@echo off
@start "" http://localhost:8889
start /d "C:\Program Files (x86)\Google\Chrome\Application\" chrome.exe -incognito http://localhost:8889
pause