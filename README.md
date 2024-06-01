Дипломный проект по профессии «Тестировщик»
=
Тестирование веб-сервиса, который предлагает купить тур по определённой цене двумя способами:
обычная оплата по дебетовой карте и выдача кредита по данным банковской карты.
------
### Начало работы:

Для получения копии этого проекта и его запуска на локальном ПК необходимо клонировать репозиторий. 
Для этого нужно:

1. Открыть [репозиторий](https://github.com/netology-code/qa-diploma)
2. Нажать на кнопку "Code"
3. В открывшемся меню выбрать вкладку SSH и скопировать ссылку на репозиторий
4. Открыть терминал (Git Bash) для папки в которой будет хранится репозиторий
5. В терминала вводим команду git clone и скопированную ссылку на репозиторий, нажимаем Enter
6. Открыть в IntelliJ IDEA клонированный проект

--------
#### Prerequisites:
1. Intelij IDEA 2023.2.2 (Community Edition)
2. Docker Desktop (version 26.1.1)
3. Браузер: Opera GX Версия 109.0.5097.100 (Версия Chromium:123.0.6312.124)
4. DBeaver 24.0.2.0
5. Git 2.42.0

------
#### Установка и запуск:

1. Запускаем Docker Desktop
2. Запускаем IDEA
3. В терминале IDEA набираем ```docker-compose down```
4. Ждем удаления 3 контейнеров и набираем ```docker-compose up --build```
5. Запускаем Jar файл:

   для mysql
   ```java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar```

   для postgresql
  ``` java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar```
6. Запускаем автотесты:

   для mysql
   ```./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"```

   для postgresql
   ```./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"```

-----
### Лицензия:
Все ПO, используемое в данном дипломном проекте, бесплатно, для домашнего и учебного использования
