const mp = new MercadoPago('TEST-c7c111d5-c814-491d-9ed3-c0e1f397f8b9', {
    locale: 'es-AR'
});

const createCheckoutButton = (preferenceId_OK) => {
    const bricksBuilder = mp.bricks();
    const generateButton = async () => {
        if (window.checkoutButton) window.checkoutButton.unmount();
        bricksBuilder.create("wallet", "wallet_container", {
            initialization: {
                preferenceId: preferenceId_OK,
            },
        });
    }
    generateButton();
}

const MP = async () => {
    try {
        const miArticulo = {
          nombreProductos: ["pesa", "bolsa", "cinta"],
            quantities: [2, 3, 4],
            unit_price: [4000, 5000, 6000]
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

        createCheckoutButton(preference);
        console.log("preferencia: " + preference);
    } catch (error) {
        alert("error: " + error);
    }
}
