$ErrorActionPreference = "Stop"

function Get-MotivaJdk {
    $candidatos = @()

    if ($env:JAVA_HOME) {
        $candidatos += $env:JAVA_HOME
    }

    $candidatos += Get-ChildItem -Directory -ErrorAction SilentlyContinue "$env:USERPROFILE\.jdks" |
        Select-Object -ExpandProperty FullName

    $candidatos += Get-ChildItem -Directory -ErrorAction SilentlyContinue "C:\Program Files\Java" |
        Select-Object -ExpandProperty FullName

    $candidatos += Get-ChildItem -Directory -ErrorAction SilentlyContinue "C:\Program Files\Eclipse Adoptium" |
        Select-Object -ExpandProperty FullName

    foreach ($pasta in $candidatos) {
        $javac = Join-Path $pasta "bin\javac.exe"
        $java = Join-Path $pasta "bin\java.exe"
        if ((Test-Path $javac) -and (Test-Path $java)) {
            return [pscustomobject]@{
                Home = $pasta
                Javac = $javac
                Java = $java
            }
        }
    }

    $javacNoPath = Get-Command javac -ErrorAction SilentlyContinue
    $javaNoPath = Get-Command java -ErrorAction SilentlyContinue
    if ($javacNoPath -and $javaNoPath) {
        return [pscustomobject]@{
            Home = ""
            Javac = $javacNoPath.Source
            Java = $javaNoPath.Source
        }
    }

    throw "JDK 17+ nao encontrado. Instale um JDK ou defina JAVA_HOME. JDKs do IntelliJ: $env:USERPROFILE\.jdks"
}
