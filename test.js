var FCM = require('fcm-push');

var serverKey = 'AAAA0r_WvnE:APA91bFVaDl_9qhHFXVh72v0_5g4zJVc7nw4D0RbcWEP_MYheMxTT_m_fQJMSQCwnjK6fMpT3O01dOgiiMekNNFT8CzcMCdMadLaumhAJOi37JgWfE10z7JRHBwr4WDHmBHf5cM5CHfv';
var fcm = new FCM(serverKey);

var message = {
    to: 'registration_token_or_topics', // required fill with device token or topics
    collapse_key: 'your_collapse_key', 
    data: {
        your_custom_data_key: 'your_custom_data_value'
    },
    notification: {
        title: 'Title of your push notification',
        body: 'Body of your push notification'
    }
};

//callback style
fcm.send(message, function(err, response){
    if (err) {
        console.log("Something has gone wrong!", err);
    } else {
        console.log("Successfully sent with response: ", response);
    }
});

//promise style
fcm.send(message)
    .then(function(response){
        console.log("Successfully sent with response: ", response);
    })
    .catch(function(err){
        console.log("Something has gone wrong!");
        console.error(err);
    })

