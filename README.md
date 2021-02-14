# Http Transaction Server

Course work by Operating System.

<details><summary>Technical assignment:</summary>
Банковская система (класс + тесты). Сервер создает отдельные потоки для интерактивной обработки запросов каждого клиента, основной поток занимается прослушиванием запросов.
</details>

## Installation

Use the maven

```bash
git clone git@github.com:OrlovDiga/transactionHttpServer.git
cd PROJECT_ROOT
mvn clean package
java -jar transaction_http_server-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Usage

This application has multiple entry points:

#### Public routes:

* POST */balance/create* - create balance account.
* POST */balance/increase* - put money to balance.
* POST */balance/reduce* - withdraw money from balance.
* GET */balance/find* - find balance account by id.
* DELETE */balance/find* - deelte balance account by id.

<details><summary>Request examples:</summary>

#### */balance/create*
`POST`
```
  response
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5",
    "moneyAmount": 0,
    "active": true
}
 ```


#### */balance/increase*
`POST`
 ```
   request
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5",
    "moneyAmount": 100
}

   response
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5",
    "moneyAmount": 100,
    "active": true
}
  ```


#### */balance/reduce*
`POST`
 ```
   request
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5",
    "moneyAmount": 30
}

   response
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5",
    "moneyAmount": 70,
    "active": true
}
  ```


#### */balance/find*
`GET`
 ```
   request
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5"
}

   response
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5",
    "moneyAmount": 70,
    "active": true
}
  ```


#### */balance/delete*
`DELETE`
 ```
   request
{
    "id": "cf6b4be0-b34c-4684-afab-4aefdb9580b5"
}

   response - empty body with statusCode 200.
  ```
</details>

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)