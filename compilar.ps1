$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
. "$PSScriptRoot\encontrar-jdk.ps1"

$jdk = Get-MotivaJdk
$ojdbc = Join-Path $PSScriptRoot "lib\ojdbc17.jar"
if (-not (Test-Path $ojdbc)) {
    throw "lib\ojdbc17.jar nao encontrado."
}

$bin = Join-Path $PSScriptRoot "bin"
New-Item -ItemType Directory -Force -Path $bin | Out-Null

$arquivos = Get-ChildItem -Recurse -Filter *.java src, test |
    ForEach-Object { $_.FullName.Substring($PSScriptRoot.Length + 1) }
$sources = Join-Path $PSScriptRoot "sources.txt"
[System.IO.File]::WriteAllLines($sources, $arquivos, [System.Text.UTF8Encoding]::new($false))

Write-Host "Compilando projeto Motiva Sprint 3..."
Write-Host "Usando: $($jdk.Javac)"
& $jdk.Javac -encoding UTF-8 -cp $ojdbc -d $bin "@$sources"
if ($LASTEXITCODE -ne 0) {
    throw "Compilacao falhou."
}

Write-Host "Compilacao concluida. Classes em bin\"
