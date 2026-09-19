$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
& "$PSScriptRoot\compilar.ps1"
& "$PSScriptRoot\executar.ps1"
