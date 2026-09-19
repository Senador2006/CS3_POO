$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
. "$PSScriptRoot\encontrar-jdk.ps1"

if (-not (Test-Path (Join-Path $PSScriptRoot "bin\main\Main.class"))) {
    & "$PSScriptRoot\compilar.ps1"
}

$jdk = Get-MotivaJdk
Write-Host "Executando main.Main..."
& $jdk.Java -cp "bin;lib\ojdbc17.jar" main.Main
exit $LASTEXITCODE
