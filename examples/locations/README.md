[![en](https://img.shields.io/badge/lang-en-blue.svg)](README.md)
[![ru](https://img.shields.io/badge/lang-ru-blue.svg)](README.ru.md)

# Location Example

This is an example of using locations in a bot.

---

When using the `/form` command, the user goes through an abstract form, filling in data and moving from one data request
to another, navigating between locations.

Main features:

- Uses the H2 database to store and update user locations
- Supports transitions between locations and returning to the previous one
- Each location has its own set of event handlers, which may differ from other locations

### Run
Required environment variables:
- `TG_BOT_TOKEN` - bot token

```bash
./gradlew run
```
_or use saved configuration `locations [run]`_