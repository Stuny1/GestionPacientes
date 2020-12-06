import * as functions from 'firebase-functions';

export const myUppercaseFunction = functions.https.onCall((data, context) => {
  return { msg: data.coolMsg.toUpperCase(), date: new Date().getTime() };
});
