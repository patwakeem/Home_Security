<template>
  <div>Hello, your ledger balance is {{ balance }}</div>
</template>
<script>
import ledgerBalance from '../data/ledger';

export default {
  name: 'Ledger',
  data() {
    return { balance: null };
  },
  created() {
    this.fetchBalance();
  },
  methods: {
    fetchBalance() {
      ledgerBalance((err, balance) => {
        if (err) {
          console.error(err);
        } else {
          this.balance = balance;
        }
      });
    },
  },
};
</script>
