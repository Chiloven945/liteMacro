<div align="center">

# liteMacro

[![Stars](https://img.shields.io/github/stars/Our-Island/liteMacro?style=flat-square&logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEiIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiI+PHBhdGggZD0iTTggLjI1YS43NS43NSAwIDAgMSAuNjczLjQxOGwxLjg4MiAzLjgxNSA0LjIxLjYxMmEuNzUuNzUgMCAwIDEgLjQxNiAxLjI3OWwtMy4wNDYgMi45Ny43MTkgNC4xOTJhLjc1MS43NTEgMCAwIDEtMS4wODguNzkxTDggMTIuMzQ3bC0zLjc2NiAxLjk4YS43NS43NSAwIDAgMS0xLjA4OC0uNzlsLjcyLTQuMTk0TC44MTggNi4zNzRhLjc1Ljc1IDAgMCAxIC40MTYtMS4yOGw0LjIxLS42MTFMNy4zMjcuNjY4QS43NS43NSAwIDAgMSA4IC4yNVoiIGZpbGw9IiNlYWM1NGYiLz48L3N2Zz4=&logoSize=auto&label=Stars&labelColor=444444&color=eac54f)](https://github.com/Our-Island/liteMacro/)
[![GitHub CI](https://img.shields.io/github/actions/workflow/status/Our-Island/liteMacro/ci.yml?style=flat-square&labelColor=444444&branch=master&label=GitHub%20CI&logo=github)](https://github.com/Our-Island/liteMacro/actions/workflows/ci.yml)
[![Hangar](https://img.shields.io/badge/Hangar-liteMacro-004ee9?style=flat-square&labelColor=444444)](https://hangar.papermc.io/Our-Island/LiteMacro)
[![Modrinth](https://img.shields.io/badge/Modrinth-liteMacro-22ff84?style=flat-square&labelColor=444444)](https://modrinth.com/plugin/litemacro/)

</div>

LiteMacro is a lightweight Velocity plugin that lets you create custom commands by chaining simple actions.
For example, you can add `/hub` as a shortcut for `/server hub`—fewer keystrokes, same result.

## Supported Actions

A macro command is defined by a list of actions. Actions are executed **in order**.

### `command`

Run a command as either the **player** or the **console**.

- `cmd` (required): The command to execute (without the leading `/`)
- `run_as` (required): Who runs the command (`player` or `console`)

### `message`

Send a chat message to the command executor.

- `text` (required): The message to send

### `delay`

Pause execution for a specified duration.

- `millis` (required): Delay time in milliseconds

### `transfer`

Transfer the executing player to another Velocity registered server.

- `target` (required): Target server name
- `message` (optional): Message to send to the player before transferring

## Quick Start

### 1) Install

1. Download the latest release from **Hangar**, **Modrinth**, or **GitHub Releases**.
2. Put the jar into your Velocity `plugins/` folder.
3. Restart the proxy.

### 2) Generate config

On first startup, LiteMacro will create a configuration file:

- `plugins/liteMacro/command.yml`

It also uses language files in:

- `plugins/liteMacro/lang/`

### 3) Choose a language

Open `plugins/liteMacro/command.yml` and set:

```yml
lang: "en_US"
```

Make sure the corresponding language file exists (for example `plugins/liteMacro/lang/en_US.properties`).

### 4) Create your first macro

Add a macro under `macros:`. Example: `/hub`

```yml
macros:
  hub:
    description: "Transfer player to hub."
    permission: "litemacro.default.hub"
    aliases: [ "lobby", "tohub" ]
    actions:
      - type: message
        options:
          text: "Transferring to hub..."
      - type: transfer
        options:
          target: "Hub"
```

### 5) Reload

After editing `command.yml`, reload the plugin:

```text
/litemacro reload
```

### 6) Test

Run the command in-game:

```text
/hub
```

If everything is configured correctly, the macro will execute actions in order.

## Contributing

Issues and pull requests are welcome.
If you have ideas, bug reports, or improvements, feel free to open an issue or submit a PR.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
