const axios = require('axios');
const request = require('request');
const fetch = require('node-fetch');

//const ngrokUrl = 'http://e06e6e98dd51.ngrok.io/mythaistar';    //Don't forget to change to the right URL before using
const ngrokUrl = 'http://manakee.de:8081/api'; 

module.exports = {
    
    /*Gets a single dish out of the Backend by its id. This method can be used for demonstration purposes.
    The method is used in the skill to give the price of a dish.*/
    async fetchDish(dishId) {
        const endpoint = ngrokUrl + '/services/rest/dishmanagement/v1/dish/';
        const url = endpoint + dishId + '/';
        console.log(url);   //in case something doesnt work and we want to test the request with postman
        
        var config = {
            timeout: 6500, //timeout call to give a response before Alexas 8sec timeout in case the server does not react
            headers: {'Accept': 'application/json'} 
        };
        
        async function getJsonResponse(url, config) {   //funciton to handle the Json Data
            const res = await axios.get(url, config);
            return res.data;
        }
        
        return await getJsonResponse(url, config).then((result) => {  //catch errors
            return result;
        }).catch((error) => {
            return null;
        });
    },
    
    /*Sends a post request to the backend with a new order status in  its body.
    This method is only for testing and demonstration purposes.*/
    async setOrderStatus(orderId, statusId) {
        
        const newStatus = '{"orderStatusId": ' + statusId + '}';    //JSON body
        const endpoint = ngrokUrl + '/services/rest/ordermanagement/v1/order/changestatus/' + orderId;    //Link to the endpoint
   
        const res = await fetch(endpoint, {
            method: "post",
            body: newStatus,
            headers: { "Content-Type": "application/json" }
        })
        return res;
      
    },
    
    /*Sends a post request to the backend to make a booking. 
    The body contains all the relevant data for the booking.*/
    async bookTable(bookingDate, name, email, assistants) {
        const booking = '{"booking":{"bookingDate":"' + bookingDate + '","name":"' + name + '","email":"' + email + '","assistants":' + assistants + "}}"; //JSON body
        const endpoint = ngrokUrl + '/services/rest/bookingmanagement/v1/booking/';
        
        const res = await fetch(endpoint, {
            method: "post",
            body: booking,
            headers: { "Content-Type": "application/json" }
        })
        
        const  result =  await res.json();
        return result;
    },
    
    /*Sends a request to the Alexa server to get the userdata for which the user gave his permission*/
    async getUserData(apiAccessToken, requestedData) {
        const endpoint = 'https://api.amazonalexa.com/v2/accounts/~current/settings/Profile.' + requestedData;
        
        const res = await fetch(endpoint, {
            method: "get",
            headers: { "Authorization": apiAccessToken }
        })
        return res;
    },
    
    /*changes the time so that the time gets written into the database correctly because there was a problem with different timezones.
    Needs to be changed when the time in germany gets switched over.*/
    parseDate(date, time) {
        let hour = time.slice(0, 2);
        if (time.charAt(0) === '0') {
            let hour = time.slice(1, 2);
        }
        else {
            let hour = time.slice(0, 2);
        }
        let correctHour = parseInt(hour, 10) - 2;
        if(correctHour < 0) {
            correctHour = 24 + correctHour;
        }
        if(correctHour < 10) {
            correctHour = '0' + correctHour;
        }
        
        const correctTime = correctHour + time.slice(2, 5);
        const dateAndTime = date + 'T' + correctTime + ':00';
        const date1 = new Date(dateAndTime);
        const dateUTC = new Date(date1.toUTCString());
        
        return dateUTC.toISOString();
    },
    
    /*Is required to save the persistent attributes. Creates and returns a persistence Adapter for the used database.*/
    getPersistenceAdapter(tableName) {
        function isAlexaHosted() {
            return process.env.S3_PERSISTENCE_BUCKET;
        }
        if (isAlexaHosted()) {
            const {S3PersistenceAdapter} = require('ask-sdk-s3-persistence-adapter');
            return new S3PersistenceAdapter({
                bucketName: process.env.S3_PERSISTENCE_BUCKET
            });
        } else {
            const {DynamoDbPersistenceAdapter} = require('ask-sdk-dynamodb-persistence-adapter');
            return new DynamoDbPersistenceAdapter({
                tableName: tableName,
                createTable: true
            });
        }
    },
    
    /*Sends a post request to the backend to make an order. 
    The body contains all the relevant information the backend needs.
    This method is making an order by using the bookingToken.
    The method is no longer used in the skill but I am leaving it in as it might be useful later.*/
    async orderDish(orderLine, bookingToken) {
        
        const order = '{"booking":{"bookingToken":"' + bookingToken + '"},"orderLines":[{' + orderLine + '}]}'; //JSON body
        
        const endpoint = ngrokUrl + '/services/rest/ordermanagement/v1/order/';
        
        const res = await fetch(endpoint, {
            method: "post",
            body: order,
            headers: { "Content-Type": "application/json" }
        })
        
        const  result =  await res.json();
        return result;
    },
    
    /*This method sends a post request to the backend to get all the dishes on the menu.
    The pageable with the page size 1000 in the body is to make sure that the user really gets all the dishes and not just the first page.*/
    async getAllDishes() {
        const body = '{"pageable":{"pageNumber":"0","pageSize":"1000","sort":[]},"categories":[]}'; //JSON body
        const endpoint = ngrokUrl + '/services/rest/dishmanagement/v1/dish/search';
        
        const res = await fetch(endpoint, {
            method: "post",
            body: body,
            headers: { "Content-Type": "application/json" }
        })
        
        const  result =  await res.json();
        return result;
    },
    
    /*Method to make an order via Alexa without the bookingtoken.
    The body contains all the relevant information the backend needs.
    Unlike the other methods in the logic this method gets its url from the handler as it uses different urls in different situations*/
    async orderAlexa(orderLine, booking, url) {
        const order = booking + '"orderLines":[' + orderLine + ']}';  //JSON body
        const endpoint = ngrokUrl + url;
        
        const res = await fetch(endpoint, {
            method: "post",
            body: order,
            headers: { "Content-Type": "application/json" }
        })
        
        const  result =  await res.json();
        return result;
    },

    async filterDishes(filterCriteria) {
        const filter = '{"categories":[{"id":"' + filterCriteria + '"}],"searchBy":"","pageable":{"pageNumber":"0","pageSize":"1000","sort":[]}}';
        const endpoint = ngrokUrl + '/services/rest/dishmanagement/v1/dish/search';

        const res = await fetch(endpoint, {
            method: "post",
            body: filter,
            headers: { "Content-Type": "application/json" }
        })
        
        const  result =  await res.json();
        return result;
    }
}