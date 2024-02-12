const { createApp } = Vue;

const mp = new MercadoPago('TEST-c7c111d5-c814-491d-9ed3-c0e1f397f8b9', {
  locale: 'es-AR'
});

const app = createApp({
  data() {
    return {
      carrito: [],
      contadorCarrito: 0,
      items: []
    };
  },
  created() {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      this.carrito = JSON.parse(savedCart);
    }
   
    this.MP()
  },
  methods: {
   
    removeFromCart(item) {
      const index = this.carrito.findIndex(cartItem => cartItem.id === item.id);
      if (index !== -1) {
        this.carrito.splice(index, 1);
        localStorage.setItem('cart', JSON.stringify(this.carrito));
      }
    },
    money(value) {
      if (value == null) {
        return '0';
      }
      return value.toLocaleString('es-AR', { style: 'currency', currency: 'ARS' });
    },
    calculateTotalCart() {
      return this.carrito.reduce((total, item) => total + this.calculateTotal(item), 0);
    },
    
    updateTotal(item) {
      item.cantidad = Math.min(item.cantidad, item.stock);
      console.log("item cantidad", item.cantidad);
    
      // Eliminar el producto del carrito si la cantidad es 0
      if (item.cantidad === 0) {
        this.removeFromCart(item);
      } else {
        localStorage.setItem('cart', JSON.stringify(this.carrito));
        console.log("numero de productos", item.cantidad);
        this.getTotalItems();
        this.updateCartCounter()
    
        // Actualizar this.carrito con el contenido actualizado del localStorage
        this.carrito = JSON.parse(localStorage.getItem('cart')) || [];
      }
    },
    updateCartCounter() {
      // Actualiza el contador de productos en el carrito
      this.contadorCarrito = this.carrito.reduce((total, item) => total + item.cantidad, 0);

      // Actualiza el contenido visual del contador en el DOM
      document.getElementById('cont').innerText = this.contadorCarrito.toString();
     
    },
    getTotalItems() {
      let totalItems = 0;

      // Recorre todos los productos en el carrito
      this.carrito.forEach(item => {
        // Asegúrate de que la cantidad sea un número válido
        const quantity = parseInt(item.cantidad) || 0;
  
        // Suma la cantidad al total general
        totalItems += quantity;
      });
  
      // Devuelve la cantidad total
      return totalItems;
    },
    calculateTotal(item) {
      if (typeof item.price === 'number' && typeof item.cantidad === 'number') {
        return item.price * item.cantidad;
      } else {
        console.error("Invalid price or quantity:", item.price, item.cantidad);
        return 0;
      }
    },
    sendMailAlert(){
      Swal.fire({
        position: "top-end",
        icon: "success",
        title: "Your bill has been sent by email",
        showConfirmButton: false,
        timer: 1500,
        width: "500px",
        height: "100px"
      });

    },
    createCheckoutButton(preferenceId_OK) {
      const bricksBuilder = mp.bricks();
      const generateButton = async () => {
        if (window.checkoutButton) window.checkoutButton.unmount();
        bricksBuilder.create("wallet", "wallet_container", {
          initialization: {
            preferenceId: preferenceId_OK,
          },
        });
      };
      generateButton();
    },
    async MP() {
      try {
        const miArticulo = {
          nombreProductos: this.carrito.map(item => item.name),
          quantities: this.carrito.map(item => item.cantidad),
          unit_price: this.carrito.map(item => item.price)
        };

        const response = await fetch("/api/purchase", {
          method: "POST",
          headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify(miArticulo),
        });

        const preference = await response.text();
        console.log("preferencia: " + preference);

        this.createCheckoutButton(preference);
      } catch (error) {
      }
    },
  },



});

app.mount('#app');
