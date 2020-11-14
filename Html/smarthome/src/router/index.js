import Vue from 'vue';
import VueRouter from 'vue-router';
import HomePage from '../components/HomePage.vue';
import Activity from '../components/Activity.vue';
import UsersGroups from '../components/UsersGroups.vue';
import Sensors from '../components/Sensors.vue';
import Cameras from '../components/Cameras.vue';
import Settings from '../components/Settings.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: HomePage,
  },
  {
    path: '/activity',
    name: 'Activity',
    component: Activity,
  },
  {
    path: '/users',
    name: 'UsersGroups',
    component: UsersGroups,
  },
  {
    path: '/sensors',
    name: 'Sensors',
    component: Sensors,
  },
  {
    path: '/cameras',
    name: 'Cameras',
    component: Cameras,
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings,
  },
];

const router = new VueRouter({
  routes,
});

export default router;
