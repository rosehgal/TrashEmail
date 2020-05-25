# TrashEmail 

TrashEmail is Java spring-boot microservice to handle webhook requests made via Telegram. The beauty of this code is that it offers minimilistic dependencies, just download, build and run sort of setup.

The application after build will run on the `port: 9090`, and before running that setup up, one. just. need to inform telegram about this webhook. The since telegram has to register this endpoint to interact with, a public IP setup is required. One can use `ngrok`, `dataplicity` or `now` to have a hosting solution.


How to register webhook with telegram :  `curl -F "url=https://<YOUR_DOMAIN>/telegram/new-message" https://api.telegram.org/bot<BOT_TOKEN>/setWebhook`
