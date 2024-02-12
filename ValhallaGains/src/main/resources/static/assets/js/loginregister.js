const app = Vue.createApp({
    data() {
        return {
            email: "",
            password: "",
            registered: false,
            name: "",
            lastName: "",
            county: "",
            city: "",
            postalCode: null,
            houseNumber: null,
            neighborhood: "",
            street: ""
        };
    },
    methods: {
        login() {
            axios.post("/api/login?email=" + this.email + "&password=" + this.password)
                .then(response => {
                    console.log(response)
                    this.clearData()
                    console.log(response.status)
                    if (response.status) {
                        window.location.href = "/web/index.html"
                    }
                    else {
                        alert("Login failed.")
                    }
                })
                .catch(response => {
                    console.log(response)
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text:  "Incorrect username or password."
                    });
                })
        },
        register() {
            axios.post("/api/client", {
                name: this.name,
                email:this.email,
                password:this.password,
                lastName:this.lastName,
                county:this.county,
                city:this.city,
                postalCode:this.postalCode,
                houseNumber:this.houseNumber,
                neighborhood:this.neighborhood,
                street:this.street
            })
            .then(response => {
                console.log("¡Hola!")
                console.log(response.data)
                Swal.fire({
                    title: "¡Goob Job!",
                    text: "Client " + this.name + " " + this.lastName + " created",
                    icon: "success"
                });

                this.clearData()

            })
            .catch(response => {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: response.response.data
                });
                console.log(response.response.data)
            })
        },
        clearData() {
            this.email = "";
            this.password = "";
            this.name = "";
            this.lastName = "";
            this.county = "";
            this.city = "";
            this.postalCode = null;
            this.houseNumber = null;
            this.neighborhood = "";
            this.street = "";
        }
    }
});

app.mount("#app");
