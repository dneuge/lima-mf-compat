# LiMa-MF Java Compatibility Library for MobiFlight Devices/Resources

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE.md)

This library establishes basic unofficial compatibility with [MobiFlight](https://www.mobiflight.com/) device firmware
and configuration files for platform-independent use with Java:

- partial serialization/deserialization of device firmware protocol messages
- partial parsing of device-side configurations as received over `GetConfig`/`Info` messages
- partial parsing of `.mcc` configuration files

All current generally available LTS versions of Java are supported (8, 11, 17, 21).

[LiMa-MF Flight Simulation Panel Connector](https://github.com/dneuge/lima-mf-panel-connector) is the
prime application using this library, which means the supported aspects of MobiFlight compatibility are
currently also driven by the needs of that application. However, as a general compatibility library can be much more
useful (e.g. reusable in other applications) and even be developed separately, it has been kept separate from the
beginning.

## License

This library is provided under [MIT license](LICENSE.md), unless declared otherwise (e.g. by source code comments).
Please be aware that dependencies (e.g. libraries and/or external data used by this project) are subject to their own
respective licenses which can affect distribution, particularly in binary/packaged form.

Development happened with reference to original MobiFlight source code, also released under MIT license at time of
writing. Sources:

- https://github.com/MobiFlight/Arduino-CmdMessenger
- https://github.com/MobiFlight/MobiFlight-FirmwareSource
- https://github.com/MobiFlight/MobiFlight-Connector

### Note on the use of/for AI

Usage for AI training is subject to individual source licenses, there is no exception. This generally means that proper
attribution must be given and disclaimers may need to be retained when reproducing relevant portions of training data.
When incorporating source code, AI models generally become derived projects. As such, they remain subject to the
requirements set out by individual licenses associated with the input used during training. When in doubt, all files
shall be regarded as proprietary until clarified.

Unless you can comply with the licenses of this project you obviously are not permitted to use it for your AI training
set. Although it may not be required by those licenses, you are additionally asked to make your AI model publicly
available under an open license and for free, to play fair and contribute back to the open community you take from.

AI tools are not permitted to be used for contributions to this project. The main reason is that, as of time of writing,
no tool/model offers traceability nor can today's AI models understand and reason about what they are actually doing.
Apart from potential copyright/license violations the quality of AI output is doubtful and generally requires more
effort to be reviewed and cleaned/fixed than actually contributing original work. Contributors will be asked to confirm
and permanently record compliance with these guidelines.

## Disclaimer

This is an unofficial project, developed independent of MobiFlight.

As this library is likely used to interface with actual hardware, there is a risk of damage being caused, for example
but not limited to e.g. the unit, health or property. **That risk is amplified** by interfacing with a firmware not
controlled by the author(s) of this library. By using this library, users accept that none of the author(s), nor
any of the author(s) of MobiFlight, nor panel manufacturers shall be held liable for any damage or harm being
potentially caused associated with the usage of this library. Carefully read the [license](LICENSE.md) for details, as
well as the license/disclaimer texts associated with MobiFlight's firmware (see MobiFlight for details).

Use of this library may void device warranties.

Applications using this library are recommended to show a similar disclaimer to inform end-users before this library is
first used to interact with a device or configuration file.

## Acknowledgments

Special thanks go out to all MobiFlight developers and the device manufacturers who chose to prefer MobiFlight over
having a proprietary firmware.
