An example of implementing an [IFTTT trigger](https://platform.ifttt.com/docs/api_reference#triggers) using the [EVRYTHNG api](https://developers.evrythng.com/) in both Node.js and Java. This is to illustrate the advantages of stateless code as described here: 

The application has one api route: `POST http://localhost:8080/ifttt/v1/triggers/temperature`. It expects the following body params:

```
{
    "triggerFields": {
        "device": "<YOUR_EVRYTHNG_THNG_ID>"
    }
}
```

When the request is made the application will use the api key specified in the `API_KEY` environment variable to request `temperature` property updates from the EVRYTHNG api. The app will then return this data in a format that would be suitable for IFTTT.

