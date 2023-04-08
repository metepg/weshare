export default {
  SUCCESS: {
    validBillForm: {
      severity: 'success',
      summary: 'Tallennus onnistui'
    },

  },
  ERROR: {
    invalidBillForm: {
      severity: 'error',
      summary: 'Virhe',
      detail: 'Lasku sisältää virheellistä tietoa'
    },
    unknownError: {
      severity: 'error',
      summary: 'Jokin meni pieleen'
    },
    paymentCancelled: {
      severity: 'error',
      summary: 'Maksaminen peruutettu'
    }
  },

}
