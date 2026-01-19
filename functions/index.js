const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.notifyOnEventAdd = functions.database
  .ref("/events/{eventId}")
  .onCreate((snapshot, context) => {

    const event = snapshot.val();

    const payload = {
      notification: {
        title: "📅 New Event Added",
        body: `${event.eventName} on ${event.date}`
      }
    };

    return admin.messaging().sendToTopic("events", payload);
  });

exports.notifyOnEventUpdate = functions.database
  .ref("/events/{eventId}")
  .onUpdate((change, context) => {

    const event = change.after.val();

    const payload = {
      notification: {
        title: "🔄 Event Updated",
        body: `${event.eventName} details were updated`
      }
    };

    return admin.messaging().sendToTopic("events", payload);
  });
