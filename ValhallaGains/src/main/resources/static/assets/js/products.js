const { createApp } = Vue;

const opt = {
  data() {
    return {
      products: [],
      categories: [],
      search: "",
      select: "all",
      sort: "none",
      favoritos: [],
      filteredProducts: [],
      category: [],
      carrito: [],
      contadorCarrito: 0,
      showModal: false,
      selectedProduct: {},
      blurBackground: false,
      menuOpen: false,
      filteredByCategory: []  // Sin corchetes aquí
    };
  },

  created() {
    this.getProducts();

  },

  methods: {
    getProducts() {
      axios.get("/api/product").then(response => {
        console.log("API Response:", response);
        this.products = response.data;
        this.categories = [...new Set(response.data.map(product => product.category.name))];

        this.filterProducts();
        // Obtén los IDs de los productos en el carrito desde localStorage
        const carritoIds = JSON.parse(localStorage.getItem("cart")) || [];

        // Filtra los productos que tienen un ID presente en el carrito
        this.filtrados = this.products.filter(product => carritoIds.includes(product.id));

        // Asegúrate de que filtrados tenga el formato correcto antes de modificar propiedades
        if (Array.isArray(this.filtrados)) {
          // Establece la propiedad 'stock' en 1 para todos los productos en filtrados
          this.filtrados.forEach(product => {
            this.$set(product, 'stock', 1);
          });
        }
        const savedCart = localStorage.getItem('cart');
        if (savedCart) {
          this.carrito = JSON.parse(savedCart);
          this.updateCartCounter();
        }
      }).catch(error => {
        console.error("Error fetching products:", error);
      });
    },
    mostrarModal(product) {
      this.product = product
      this.showModal = true
      
    },
    cerrarModal() {

      this.showModal = false

    },
    toggleMenu() {
      this.menuOpen = !this.menuOpen;
    },
    checkTablet() {
      // Actualizar 'isTablet' según el ancho de la ventana
      this.isTablet = window.innerWidth <= 375; // Ajusta el valor según sea necesario
    },

    money(value) {
      if (value == null) {
        return '0';
      }
      return value.toLocaleString('es-AR', { style: 'currency', currency: 'ARS' });
    },

    addedToCart() {
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Your product has been added",
        showConfirmButton: false,
        timer: 1500,
        width: "500px",
        height: "100px"
      });
    },
    noMoreStock() {
      Swal.fire({
        position: "top-end",
        icon: "error",
        title: "No more products",
        showConfirmButton: false,
        timer: 1500,
        width: "500px",
        height: "100px"
      });
    },
    filterProducts() {
      this.filteredProducts = this.products.filter((product) => {
        const isCategoryMatch = this.select === "all" || product.category.name === this.select;
        const isNameMatch = product.name.toLowerCase().includes(this.search.toLowerCase());

        return isCategoryMatch && isNameMatch;
      });
      if (this.sort === "asc") {
        this.filteredProducts.sort((a, b) => a.price - b.price);
      } else if (this.sort === "desc") {
        this.filteredProducts.sort((a, b) => b.price - a.price);
      }
    },
    addToCartShop(product) {
      const existingProductIndex = this.carrito.findIndex(item => item.id === product.id);

      if (existingProductIndex !== -1) {
        if (this.carrito[existingProductIndex].cantidad < product.stock) {
          this.carrito[existingProductIndex].cantidad += 1;
        } else {
          console.warn('¡No se puede agregar más de la cantidad disponible en el stock!');
          this.noMoreStock();  // Muestra el mensaje de error cuando se excede el stock
          return;  // No actualices el contador
        }
      } else {
        this.addedToCart()
        const newProduct = { ...product, cantidad: 1 };
        this.carrito.push(newProduct);
      }
      // Actualiza el contador solo cuando se agrega un nuevo producto al carrito

      this.updateCartCounter()
      localStorage.setItem("cart", JSON.stringify(this.carrito));
    },

    updateCartCounter() {
      // Actualiza el contador de productos en el carrito
      this.contadorCarrito = this.carrito.reduce((total, item) => total + item.cantidad, 0);

      // Actualiza el contenido visual del contador en el DOM
      document.getElementById('cont').innerText = this.contadorCarrito.toString();
     
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
        });}

  },


};

const app = createApp(opt);
app.mount("#app");