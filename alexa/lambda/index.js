const Alexa = require('ask-sdk-core');
const logic = require('./logic');
const interceptors = require('./interceptors');
const constants = require('./constants');
var persistenceAdapter = logic.getPersistenceAdapter('PersistenAttributes');

/*Is  executed when the skill is started. Initiates some session attributes and welcomes the user.*/
const LaunchRequestHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'LaunchRequest';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const speakOutput = requestAttributes.t('WELCOME_MSG');
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        
        sessionAttributes['orderLine'] = '';
        sessionAttributes['isInhouse'] = false;
        sessionAttributes['delivery'] = ' ';
        sessionAttributes['tableId'] = 1;
        sessionAttributes['dish'] = 2;
        sessionAttributes['shopping-cart'] = ' ';

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*Can be used by the waiter to configure the Alexa device as inhouse device*/
const SetInhouseIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'SetInhouseIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        
        sessionAttributes['isInhouse'] = true;
        const speakOutput = requestAttributes.t('INHOUSE_ACTIVATED_MSG');
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

const SetTableIdIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'SetTableIdIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const id = Alexa.getSlotValue(handlerInput.requestEnvelope, 'tableId');
        
        sessionAttributes['tableId'] = id;
        const speakOutput = requestAttributes.t('ID_SET_MSG')  + id + '.';
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

const ListExtrasIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'ListExtrasIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const data = await logic.fetchDish(sessionAttributes['dish']);
        const extras = data.extras;
        let extraNames = extras[0]['name'];
        const contentLength = extras.length;
        
        for(let i = 1; i < contentLength; i++) {
            extraNames = extraNames + ', ' + extras[i]['name'];
        }

        const speakOutput = requestAttributes.t('EXTRAS_MSG') + extraNames;

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

const FilterDishesIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'FilterDishesIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const filterCriteria = Alexa.getSlot(handlerInput.requestEnvelope, 'filterCriteria');
        const criteriaId = filterCriteria.resolutions.resolutionsPerAuthority[0].values[0].value.id;
        
        const filteredDishes = await logic.filterDishes(criteriaId);
        const contentLength = filteredDishes['content'].length;
        let dishNames = filteredDishes['content'][0]['dish']['name'];
        
        for(let i = 1; i < contentLength; i++) {
            dishNames = dishNames + ', ' + filteredDishes['content'][i]['dish']['name'];
        }
        
        let speakOutput = requestAttributes.t('DISHES_MSG') + dishNames;
        if(filteredDishes['content'][0]['dish']['name'] === 'Beer') {
           speakOutput = requestAttributes.t('BVG_MSG') + dishNames;
        }
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*If the device is not set to inhouse mode and the customer wants to order he will be asked if he wants his order delivered or if he wants to eat in the restaurant.
When the delivery option is set this will be saved and the user will not be asked for it again.*/
const ChooseDeliveryIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'ChooseDeliveryIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const deliveryOption = Alexa.getSlotValue(handlerInput.requestEnvelope, 'delivery');
        
        sessionAttributes['delivery'] = deliveryOption;
        const speakOutput = requestAttributes.t('DELIVERY_CHOSEN_MSG');
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .addDelegateDirective({
                name: 'FinishOrderIntent',
                confirmationStatus: 'NONE',
                slots: {}
            })
            .getResponse();
    }
};

/*Gets a single dish out of the database by its id.
This handler was only used for testing and demonstration purposes and is not used in the actual skill.*/
const GetDishesIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'GetDishesIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const id = Alexa.getSlotValue(handlerInput.requestEnvelope, 'id')
        const dishJson = await logic.fetchDish(id); //.dish.name;    //trying to get the name of the dish out of the Json. Not sure if this is correct.
        console.log(dishJson);  //for testing purposes
        const speakOutput = requestAttributes.t('REQUEST_DISH_MSG') + dishJson.dish.name;
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*Gives the price of a dish*/
const GetPriceIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'GetPriceIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const dishId = Alexa.getSlot(handlerInput.requestEnvelope, 'dish').resolutions.resolutionsPerAuthority[0].values[0].value.id;
        const dishName = Alexa.getSlotValue(handlerInput.requestEnvelope, 'dish');
        const dishJson = await logic.fetchDish(dishId);
        const speakOutput = requestAttributes.t('PRICE_MSG') + dishName + requestAttributes.t('IS_MSG') + dishJson.dish.price + requestAttributes.t('EUROS_MSG');
        sessionAttributes['dish'] = dishId;
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*Can change the status of an order. Can be used for testing and demonstration purposes. Has no function in the actual skill.*/
const SetStatusIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'SetStatusIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const orderId = Alexa.getSlotValue(handlerInput.requestEnvelope, 'orderNumber');
        const status = Alexa.getSlot(handlerInput.requestEnvelope, 'status');
        const statusId = status.resolutions.resolutionsPerAuthority[0].values[0].value.id;
        const statusName = status.value;
        const res = await logic.setOrderStatus(orderId, statusId);
        const speakOutput = requestAttributes.t('STATUS_MSG') + orderId + requestAttributes.t('SUCCESS_MSG') + statusName;
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*Books a table in the restaurant and writes the booking into the database.*/
const BookTableIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'BookTableIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        
        const date = Alexa.getSlotValue(handlerInput.requestEnvelope, 'date');
        const time = Alexa.getSlotValue(handlerInput.requestEnvelope, 'time');
        const assistants = Alexa.getSlotValue(handlerInput.requestEnvelope, 'assistants');
        const name = sessionAttributes['name'] || 'Dieter'; //Uses the actual Name of the person if it has permission. If not it uses the mock name after the ||
        const email = sessionAttributes['email'] || 'dieter.bohlen@rtl.de'; //same as for name
        const dateUTC = logic.parseDate(date, time);
        const res = await logic.bookTable(dateUTC, name, email, assistants);
        const bookingToken = res.bookingToken;
        sessionAttributes['bookingToken'] = bookingToken;
        const speakOutput = requestAttributes.t('BOOKING_MSG');
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .addDelegateDirective({
                name: 'AddOrderIntent',
                confirmationStatus: 'NONE',
                slots: {}
            })
            .getResponse();
    }
};

const ReadShoppingCartIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'ReadShoppingCartIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const speakOutput = requestAttributes.t('CART_MSG') + sessionAttributes['shopping-cart'];

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

const DeleteShoppingCartIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'DeleteShoppingCartIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        sessionAttributes['shopping-cart'] = ' ';
        sessionAttributes['orderLine'] = '';
        const speakOutput = requestAttributes.t('CART_DELETED_MSG');

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};


/*Step 1 of the ordering process. Adds a dish to the orderline.*/
const OrderDishIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'OrderDishIntent';
    },
    
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const dishName = Alexa.getSlotValue(handlerInput.requestEnvelope, 'dish');
        const number = Alexa.getSlotValue(handlerInput.requestEnvelope, 'number');
        const dish = Alexa.getSlot(handlerInput.requestEnvelope, 'dish');
        const dishId = dish.resolutions.resolutionsPerAuthority[0].values[0].value.id;
        const extra = Alexa.getSlot(handlerInput.requestEnvelope, 'extra');
        const extraId = extra.resolutions.resolutionsPerAuthority[0].values[0].value.id;
        const extraName = Alexa.getSlotValue(handlerInput.requestEnvelope, 'extra');
        const numberExtras = Alexa.getSlotValue(handlerInput.requestEnvelope, 'numberExtras');
        const orderLines = sessionAttributes['orderLine'];
        sessionAttributes['dish'] = dishId;
        sessionAttributes['shopping-cart'] = sessionAttributes['shopping-cart'] + ' ' + dishName + ',';
        
        //Builds the orderline either with or without extras, depending on the customes choice
        if(extraId === '2') {
            sessionAttributes['orderLine'] = sessionAttributes['orderLine'] + '{"orderLine":{"dishId":' + dishId + ',"amount":' + number + ',"comment":""},"extras":[]}';
        }
        else {
            sessionAttributes['orderLine'] = sessionAttributes['orderLine'] + '{"orderLine":{"dishId":' + dishId + ',"amount":' + number + ',"comment":""},"extras":[{"id":' + extraId + '}]}';
            sessionAttributes['shopping-cart'] = sessionAttributes['shopping-cart'] + requestAttributes.t('ADD_EXTRA_MSG') + extraName + ',';
        }
        
        const speakOutput = requestAttributes.t('THANK_ORDER_MSG');
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .addDelegateDirective({
                name: 'AddDishToOrderIntent',
                confirmationStatus: 'NONE',
                slots: {}
            })
            .getResponse();
            
       
    }
};

/*Step 2 of the ordering process.
Asks the user if he wants to order another dish. If yes he is redirected to the order dish intent and step 1 will be repeated and if no it leads to step 3, the finish order intent.*/
const AddDishToOrderIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'AddDishToOrderIntent';
    },
    handle(handlerInput) {
        const response = Alexa.getSlot(handlerInput.requestEnvelope, 'yesno');
        const responseId = response.resolutions.resolutionsPerAuthority[0].values[0].value.id;
        const speakOutput = '';
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        
        if(responseId === '0') {
            if(sessionAttributes['orderLine'] !== '') {
                sessionAttributes['orderLine'] = sessionAttributes['orderLine'] + ',';
            }
            return handlerInput.responseBuilder
            .speak(speakOutput)
            .addDelegateDirective({
                name: 'OrderDishIntent',
                confirmationStatus: 'NONE',
                slots: {}
            })
            .getResponse();
        }
        else if(responseId === '1') {
            return handlerInput.responseBuilder
            .speak(speakOutput)
            .addDelegateDirective({
                name: 'FinishOrderIntent',
                confirmationStatus: 'NONE',
                slots: {}
            })
            .getResponse();
        }
        else {
            const msg = requestAttributes.t('ERR_MSG');
            return handlerInput.responseBuilder
            .speak(msg)
            .reprompt(msg)
            .getResponse();
        }
    }
};

/*Step 3 of the ordering process.
Also checks if the user wants it delivered, order in the restaurant or is in the restaurant at the moment.
The lines which are commented out are for the order with a booking token. I'm leaving them in the code as they might be useful some time.*/
const FinishOrderIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'FinishOrderIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const name = sessionAttributes['name'] || 'sample-name';
        const email = sessionAttributes['email'] || 'sample@mail.de';
        var speakOutput = requestAttributes.t('ORDER_SUCCESS_MSG');
        var booking = '';
        var url = '';
        const id = sessionAttributes['tableId'];
        
        if (sessionAttributes['isInhouse'] === true) {
            booking = '{"booking":{"tableId":' + id + '},';
            url = '/services/rest/ordermanagement/v1/alexaorderinhouse';
        }
        else {
            if  (sessionAttributes['delivery'] === ' ') {
                speakOutput = ' ';
                return handlerInput.responseBuilder
                    .speak(speakOutput)
                    .addDelegateDirective({
                        name: 'ChooseDeliveryIntent',
                        confirmationStatus: 'NONE',
                        slots: {}
                    })
                    .getResponse();
            }
            else if (sessionAttributes['delivery'] === 'restaurant') {
                booking = '{"booking":{"email":"' + email + '"},';
                url = '/services/rest/ordermanagement/v1/alexaorderfromhome';
            }
            else {
                booking = '{"booking":{"name":"' + name + '","email":"' + email + '"},';
                url = '/services/rest/ordermanagement/v1/alexadeliveryorderfromhome';
            }
        }
        
        const result = await logic.orderAlexa(sessionAttributes['orderLine'], booking, url);
        const resCode = result.code;
    
        if(resCode === "TechnicalError") {
            speakOutput = requestAttributes.t('ERROR_BOOKING_MSG');
        }
        else {
            speakOutput = requestAttributes.t('ORDER_SUCCESS_MSG');
        sessionAttributes['orderLine'] = '';
        sessionAttributes['delivery'] = ' ';
        
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*Asks the customer if he wants to add another dish to his order and redirects to the correct intent*/
const AddOrderIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'AddOrderIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const response = Alexa.getSlot(handlerInput.requestEnvelope, 'question');
        const responseId = response.resolutions.resolutionsPerAuthority[0].values[0].value.id;
        
        if(responseId === '1') {
            const speakOutput = requestAttributes.t('THANK_MSG');
            return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
        }
        else if(responseId === '0') {
            const speakOutput = '';
            return handlerInput.responseBuilder
            .speak(speakOutput)
            .addDelegateDirective({
                name: 'OrderDishIntent',
                confirmationStatus: 'NONE',
                slots: {}
            })
            .getResponse();
        }
        else {
            const speakOutput = requestAttributes.t('ERR_MSG');
            return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
        }
    }
};

/*This handler lists all the available dishes in the restaurant*/
const GetAllDishesIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'GetAllDishesIntent';
    },
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const dishes = await logic.getAllDishes();
        const contentLength = dishes['content'].length;
        let dishNames = dishes['content'][0]['dish']['name'];
        
        for(let i = 1; i < contentLength; i++) {
            dishNames = dishNames + ', ' + dishes['content'][i]['dish']['name'];
        }
        
        const speakOutput = requestAttributes.t('DISHES_MSG') + dishNames;
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*With this handler the user can order beverages separately from the dishes.*/
const OrderBeverageIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'OrderBeverageIntent';
    },
    
    async handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const sessionAttributes = handlerInput.attributesManager.getSessionAttributes();
        const beverage = Alexa.getSlot(handlerInput.requestEnvelope, 'beverage');
        const beverageName = Alexa.getSlotValue(handlerInput.requestEnvelope, 'beverage');
        const beverageId = beverage.resolutions.resolutionsPerAuthority[0].values[0].value.id;
        const number = Alexa.getSlotValue(handlerInput.requestEnvelope, 'number');
        sessionAttributes['shopping-cart'] = sessionAttributes['shopping-cart'] + ' ' + beverageName + ',';
        
        sessionAttributes['orderLine'] = sessionAttributes['orderLine'] + '{"orderLine":{"dishId":' + beverageId + ',"amount":' + number + ',"comment":""},"extras":[]}';
        
        const speakOutput = requestAttributes.t('BEVERAGE_MSG');
        return handlerInput.responseBuilder
            .speak(speakOutput)
            .addDelegateDirective({
                name: 'AddDishToOrderIntent',
                confirmationStatus: 'NONE',
                slots: {}
            })
            .getResponse();
    }
};

/*This handler responds if the user asks for help.
It gives the user information of the functions this skill has.*/
const HelpIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.HelpIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const speakOutput = requestAttributes.t('HELP_MSG');

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/*This handler makes it possible to stop the skill at any point.*/
const CancelAndStopIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && (Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.CancelIntent'
                || Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.StopIntent');
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const speakOutput = requestAttributes.t('GOODBYE_MSG');

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .getResponse();
    }
};
/* *
 * FallbackIntent triggers when a customer says something that doesn’t map to any intents in your skill
 * It must also be defined in the language model (if the locale supports it)
 * This handler can be safely added but will be ingnored in locales that do not support it yet 
 * */
const FallbackIntentHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest'
            && Alexa.getIntentName(handlerInput.requestEnvelope) === 'AMAZON.FallbackIntent';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const speakOutput = requestAttributes.t('ERR_MSG');

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};
/* *
 * SessionEndedRequest notifies that a session was ended. This handler will be triggered when a currently open 
 * session is closed for one of the following reasons: 1) The user says "exit" or "quit". 2) The user does not 
 * respond or says something that does not match an intent defined in your voice model. 3) An error occurs 
 * */
const SessionEndedRequestHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'SessionEndedRequest';
    },
    handle(handlerInput) {
        console.log(`~~~~ Session ended: ${JSON.stringify(handlerInput.requestEnvelope)}`);
        // Any cleanup logic goes here.
        return handlerInput.responseBuilder.getResponse(); // notice we send an empty response
    }
};
/* *
 * The intent reflector is used for interaction model testing and debugging.
 * It will simply repeat the intent the user said. You can create custom handlers for your intents 
 * by defining them above, then also adding them to the request handler chain below 
 * */
const IntentReflectorHandler = {
    canHandle(handlerInput) {
        return Alexa.getRequestType(handlerInput.requestEnvelope) === 'IntentRequest';
    },
    handle(handlerInput) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const intentName = Alexa.getIntentName(handlerInput.requestEnvelope);
        const speakOutput = requestAttributes.t('REFLECT_MSG');

        return handlerInput.responseBuilder
            .speak(speakOutput)
            //.reprompt('add a reprompt if you want to keep the session open for the user to respond')
            .getResponse();
    }
};
/**
 * Generic error handling to capture any syntax or routing errors. If you receive an error
 * stating the request handler chain is not found, you have not implemented a handler for
 * the intent being invoked or included it in the skill builder below 
 * */
const ErrorHandler = {
    canHandle() {
        return true;
    },
    handle(handlerInput, error) {
        const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
        const speakOutput = requestAttributes.t('PROBLEM_MSG');
        console.log(`~~~~ Error handled: ${JSON.stringify(error)}`);

        return handlerInput.responseBuilder
            .speak(speakOutput)
            .reprompt(speakOutput)
            .getResponse();
    }
};

/**
 * This handler acts as the entry point for your skill, routing all request and response
 * payloads to the handlers above. Make sure any new handlers or interceptors you've
 * defined are included below. The order matters - they're processed top to bottom 
 * */
exports.handler = Alexa.SkillBuilders.custom()
    .addRequestHandlers(
        LaunchRequestHandler,
        SetTableIdIntentHandler,
        GetDishesIntentHandler,
        SetStatusIntentHandler,
        GetPriceIntentHandler,
        BookTableIntentHandler,
        OrderDishIntentHandler,
        FinishOrderIntentHandler,
        AddOrderIntentHandler,
        AddDishToOrderIntentHandler,
        GetAllDishesIntentHandler,
        FilterDishesIntentHandler,
        ListExtrasIntentHandler,
        ReadShoppingCartIntentHandler,
        DeleteShoppingCartIntentHandler,
        HelpIntentHandler,
        CancelAndStopIntentHandler,
        FallbackIntentHandler,
        SessionEndedRequestHandler,
        OrderBeverageIntentHandler,
        SetInhouseIntentHandler,
        ChooseDeliveryIntentHandler,
        IntentReflectorHandler)
    .addErrorHandlers(
        ErrorHandler)
    .addRequestInterceptors(
        interceptors.LoadNameRequestInterceptor,
        interceptors.LoadEmailRequestInterceptor,
        interceptors.LoadAttributesRequestInterceptor,
        interceptors.SaveAttributesResponseInterceptor,
        interceptors.LocalizationInterceptor)
    .withCustomUserAgent('sample/my-thai-star/v1.0')
    .withPersistenceAdapter(persistenceAdapter)
    .lambda();