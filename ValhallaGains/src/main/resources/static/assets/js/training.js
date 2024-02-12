const { createApp } = Vue;

const opt = {
  data() {
    return {

    };
  },
  methods: {
    trainingSelect(training) {
      axios.patch("/api/client/training?trainingType=" + training)
        .then(response => {
          Swal.fire({
            title: "Would you like this workout?",
            text: "Our professionals will create a specific plan for you.!",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "I want"
          }).then((result) => {
            if (result.isConfirmed) {

              Swal.fire({
                title: "Success",
                text: " Remember that plans are provided after the evaluation at the gym.",
                icon: "success"
              });

              setTimeout(() => {
                window.location.reload();
              }, 3000);
            }
          })
        })
        .catch(error => {
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Something went wrong!",
          });
          console.log(error)
        });
    },
    logout(){
      axios.post("/api/logout")
          .then(response => {
              console.log(response)
              if (response.status == 200) {
                  window.location.href = "/web/index.html"
              }
          })
          .catch(error => {
            console.error("Error during logout:", error);
        });
}
  },
  }


const app = createApp(opt);
app.mount("#app");