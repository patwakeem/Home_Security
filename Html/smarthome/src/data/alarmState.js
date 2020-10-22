const images = require.context('../assets', true, /\.png$/);

const alarmStateObject = [
  {
    id: 1,
    alarmState: 0,
    title: 'Disarm',
    src: images('./disarm.png'),
    selected: true,
  },
  {
    id: 2,
    alarmState: 1,
    title: 'Arm In',
    src: images('./arm.png'),
    selected: true,
  },
  {
    id: 3,
    alarmState: 2,
    title: 'Arm Away',
    src: images('./arm.png'),
    selected: true,
  },
  {
    id: 4,
    alarmState: 0,
    title: 'Disarm',
    src: images('./disarm_disabled.png'),
    selected: false,
  },
  {
    id: 5,
    alarmState: 1,
    title: 'Arm In',
    src: images('./arm_disabled.png'),
    selected: false,
  },
  {
    id: 6,
    alarmState: 2,
    title: 'Arm Away',
    src: images('./arm_disabled.png'),
    selected: false,
  },
];
export default alarmStateObject;
