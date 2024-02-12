const { createApp } = Vue;

const opt = {
  data() {
    return {

    };
  },
  methods: {
    logout(){
      axios.post("/api/logout")
          .then(response => {
              console.log(response)
              if (response.status == 200) {
                  window.location.href = "/web/index.html"
              }
          })
  },
  }
}

const app = createApp(opt);
app.mount("#app");