---
marp: true
theme: uncover
class: invert
style: |
  gr { color: #d5c4a1 }
  dgr { color: #a89984; font-size: 0.6em; }
  a:link: { color: #458588 }
  g { color: #b8bb26 }
  :root { 
    --sequence-theme: hand
    --color-background: #282828;
    --color-background-code: #1d2021;
    --color-foreground: #fbf1c7;
  }

---
## Разработка модификации для игры <g>Minecraft</g>
<gr>Подготовил:</gr> Тришин Андрей

---
>**Модификация** (моддинг игр, прог. жарг. «мод») — изменение компьютерной игры, осуществлённое кем-либо, кроме её разработчиков.

<gr>Источник:</gr> Википедия

---
>Самой продаваемой видеоигрой на сегодняшний день является <g>Minecraft</g>, игра-песочница, выпущенная Mojang в мае 2009 года

<gr>Источник:</gr> Википедия

---
![bg right w:80%](img/client_server.png)
Сетевая игра в <g>Minecraft</g> реализована по принципу клиент-сервер

---
![bg left](https://i.redd.it/sd1egtzh12d21.png)
<gr>Историческое фото:</gr>
LAN-вечеринка игроков в <g>Minecraft</g>.
<gr>2012 год</gr>
<dgr>Источник: https://bit.ly/3hRVoOt</dgr>

---
#### Философия <g>Unix</g> гласит:
>* Пишите программы, которые делают что-то одно и делают это хорошо.
>* Пишите программы, которые бы работали вместе.

<gr>Источник:</gr> Википедия

---
![bg right w:80%](img/ngrok.png)
Принцип работы <g>ngrok</g>

---
![bg left w:60%](img/fabricmc.png)
Одним из самых популярных инструментов для моддинга Minecraft является <g>FabricMC</g>

---
![bg w:90%](img/screenshot1.png)

---
### Команды мода:
* <gr>/tunnel open</gr> - открывает туннель
* <gr>/tunnel close</gr> - закрывает туннель
* <gr>/tunnel token <g>[токен]</g></gr> - задаёт токен ngrok
* <gr>/tunnel region <g>[регион]</g></gr> - задаёт регион ngrok
* <gr>/tunnel copyip</gr> - копирует IP туннеля в буффер обмена
* <gr>/tunnel options</gr> - открывает меню настроек мода

---
# Спасибо за внимание