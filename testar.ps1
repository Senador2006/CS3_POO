$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
. "$PSScriptRoot\encontrar-jdk.ps1"

if (-not (Test-Path (Join-Path $PSScriptRoot "bin\br\com\motiva\TestSuite.class"))) {
    & "$PSScriptRoot\compilar.ps1"
}

$jdk = Get-MotivaJdk
Write-Host "Executando testes da Sprint 2..."
& $jdk.Java -cp "bin;lib\ojdbc17.jar" br.com.motiva.TestSuite
exit $LASTEXITCODE
