import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    activeAlarm: null,
    alarmOn: false,
    alarmState: null,
    loggedIn: false,
    username: null,
  },
  mutations: {
    setAlarmState(state, alarmState) {
      state.alarmState = alarmState;
    },
    setAlarmOn(state, alarmOn) {
      state.alarmOn = alarmOn;
    },
    setLoggedIn(state, isLoggedIn) {
      state.loggedIn = isLoggedIn;
    },
    setUsername(state, user) {
      state.username = user;
    },
    storeActiveAlarm(state, activeAlarm) {
      state.activeAlarm = activeAlarm;
    },
  },
  actions: {
  },
  modules: {
  },
});
