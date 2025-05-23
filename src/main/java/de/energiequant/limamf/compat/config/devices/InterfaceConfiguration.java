package de.energiequant.limamf.compat.config.devices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.energiequant.limamf.compat.protocol.GetConfigMessage;
import de.energiequant.limamf.compat.utils.Maps;

/**
 * Describes all hardware components ("devices") configured on a MobiFlight module.
 * <p>
 * Hardware descriptions are provided by module manufacturers and persisted on each MobiFlight module. These
 * descriptions assign component types (such as digital signals/buttons or rotary encoder inputs) and designate pins to
 * be used for input or output. They are also used to address/name components ("devices" in MobiFlight terms) on a first
 * level. More detailed information, such as naming channels of multiplexed components, is beyond the scope of this
 * hardware-level description and thus requires further host-side configuration to make sense of.
 * </p>
 */
public class InterfaceConfiguration {
    private final List<DeviceConfiguration> devices;

    private static final char PARAMETER_DELIMITER = '.';

    private static final Map<DeviceType, Function<String, DeviceConfiguration>> DECODERS_BY_DEVICE_TYPE = Maps.createEnumMap(
        DeviceType.class,
        Maps.entry(DeviceType.ENCODER, EncoderConfiguration::parseFirmwareFormat),
        Maps.entry(DeviceType.DIGITAL_INPUT_MULTIPLEXER, DigitalInputMultiplexerConfiguration::parseFirmwareFormat),
        Maps.entry(DeviceType.OUTPUT, OutputConfiguration::parseFirmwareFormat)
    );

    private InterfaceConfiguration(List<DeviceConfiguration> devices) {
        this.devices = Collections.unmodifiableList(devices);
    }

    /**
     * Returns all described hardware components (devices in MobiFlight terms).
     *
     * @return all hardware components described by this configuration
     */
    public List<DeviceConfiguration> getDevices() {
        return devices;
    }

    /**
     * Parses the given string to an {@link InterfaceConfiguration} according to the given protocol encoding used by
     * MobiFlight firmware (e.g. received via {@link GetConfigMessage}).
     *
     * @param s protocol encoding used by MobiFlight firmware
     * @return decoded hardware configuration
     */
    public static InterfaceConfiguration parseFirmwareFormat(String s) {
        List<DeviceConfiguration> devices = new ArrayList<>();

        String[] deviceStrings = s.split(":");
        for (int i = 0; i < deviceStrings.length; i++) {
            String deviceString = deviceStrings[i];
            if (deviceString.isEmpty()) {
                if (i == deviceStrings.length - 1) {
                    // only the last device string may be empty due to termination of previous name
                    break;
                } else {
                    throw new IllegalArgumentException("empty device string found in unexpected place");
                }
            }

            int parameterDelimiter = deviceString.indexOf(PARAMETER_DELIMITER);
            if (parameterDelimiter < 0) {
                throw new IllegalArgumentException("missing parameter delimiter on entry #" + i + ": \"" + deviceString + "\"");
            }
            if (parameterDelimiter == 0) {
                throw new IllegalArgumentException("blank type ID on entry #" + i + ": \"" + deviceString + "\"");
            }

            int typeEncoded;
            try {
                typeEncoded = Integer.parseUnsignedInt(deviceString.substring(0, parameterDelimiter));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("bad type ID on entry #" + i + ": \"" + deviceString + "\"", ex);
            }

            DeviceType type = DeviceType.fromFirmwareEncoding(typeEncoded).orElse(null);
            if (type == null) {
                throw new IllegalArgumentException("unknown type ID " + typeEncoded + " on entry #" + i + ": \"" + deviceString + "\"");
            }

            Function<String, DeviceConfiguration> decoder = DECODERS_BY_DEVICE_TYPE.get(type);
            if (decoder == null) {
                throw new IllegalArgumentException("unhandled type " + type + " on entry #" + i + ": \"" + deviceString + "\"");
            }

            String remainder = deviceString.substring(parameterDelimiter + 1);
            DeviceConfiguration device;
            try {
                device = decoder.apply(remainder);
            } catch (Exception ex) {
                throw new IllegalArgumentException("decoding " + type + " failed for entry #" + i + ": \"" + deviceString + "\"", ex);
            }

            devices.add(device);
        }

        return new InterfaceConfiguration(devices);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InterfaceConfiguration(");

        for (int i = 0; i < devices.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append("#");
            sb.append(i);
            sb.append("=");

            sb.append(devices.get(i));
        }

        sb.append(")");

        return sb.toString();
    }
}
