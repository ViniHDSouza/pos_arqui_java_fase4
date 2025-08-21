@echo off
setlocal enabledelayedexpansion

rem Limpa o arquivo de saída se já existir
if exist fontes.txt del fontes.txt

rem Procura arquivos .java, .properties, .yml recursivamente
for /R %%f in (*.java *.properties *.yml) do (
    echo === %%f === >> fontes.txt
    type "%%f" >> fontes.txt
    echo. >> fontes.txt
)

rem Inclui o Dockerfile da raiz, se existir
if exist Dockerfile (
    echo === Dockerfile === >> fontes.txt
    type Dockerfile >> fontes.txt
    echo. >> fontes.txt
)

echo Conteúdo dos arquivos copiado para fontes.txt
pause
