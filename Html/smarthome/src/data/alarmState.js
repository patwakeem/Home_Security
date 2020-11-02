const images = require.context('../assets', true, /\.png$/);

const alarmStateObject = [
  {
    id: 1,
    alarmState: 0,
    title: 'Disarm',
    src: images('./home_disarm.png'),
    selected: true,
  },
  {
    id: 2,
    alarmState: 1,
    title: 'Arm In',
    src: images('./home_locked_in.png'),
    selected: true,
  },
  {
    id: 3,
    alarmState: 2,
    title: 'Arm Away',
    src: images('./home_locked_away.png'),
    selected: true,
  },
  {
    id: 4,
    alarmState: 0,
    title: 'Disarm',
    src: images('./home_disarm_disabled.png'),
    selected: false,
  },
  {
    id: 5,
    alarmState: 1,
    title: 'Arm In',
    src: images('./home_locked_in_disabled.png'),
    selected: false,
  },
  {
    id: 6,
    alarmState: 2,
    title: 'Arm Away',
    src: images('./home_locked_away_disabled.png'),
    selected: false,
  },
];
export default alarmStateObject;
