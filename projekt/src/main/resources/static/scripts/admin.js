//funkcija za prikaz korisnika
function userManager() {

    //hvatamo container
    const container = document.querySelector(".dynamicContainer");

    //prvo trebamo ubiti svu djecu u kontejneru
    container.innerHTML = '';

    //u container doajemo kartice s korisnicima
    const korisnici = window.korisnici;
    console.log(korisnici)

    korisnici.forEach(korisnik => {
        const card = document.createElement("div");
        card.className = "korisnik-card";

        card.innerHTML = `
            <div>
                <div class="user-flag">
                    ${korisnik.email == null ? `<h3>${korisnik.username}</h3>` : ``}
                    ${korisnik.email != null ? `<h3>${korisnik.email}</h3>` : ``}
                </div>

                <div class="user-box">
                    <p>Role: ${korisnik.role}</p>
                </div>

                <div class="deleteDugme">
                    <form action="/admin/delete" method="post">
                        <input type="hidden" name="username" value="${korisnik.username}">
                        <button type="submit" class="deleteButton">Delete user</button>
                    </form>
                </div>
            </div>
`;

        container.appendChild(card);
    });
};

//funkcija za izmjenu cijene mjesečne članarine
function subscriptionManager() {
    //hvatamo container
    const container = document.querySelector(".dynamicContainer");

    //prvo trebamo ubiti svu djecu u kontejneru
    container.innerHTML = '';

    const card = document.createElement("div");
    card.className = "priceChanger"

    const trenutno = [
        {
            cijenaMjesec: window.price.monthly,
            cijenaGod: window.price.yearly
        }
    ];

    card.innerHTML = `
                    <div>
                        <div class = "title-card">
                            <h3>Change prices</h3>
                        </div>
                        <div class="oldBox">
                            <p>Current monthly price: ${trenutno[0].cijenaMjesec} €</p>
                            <p>Current yearly price: ${trenutno[0].cijenaGod} €</p>
                        </div>
                        <form class = "newBox" action="/admin/price" method="post">
                            <label>
                                <p>New price(month): </p>
                                <input name="monthly" type = "number" min = "0" step = "0.01" placeholder = "0.00" />
                            </label>

                            <label>
                                <p>New price(year): </p>
                                <input name="yearly" type = "number" min = "0" step = "0.01" placeholder = "0.00" />
                            </label>
                            
                            <div class="bttnWrap">
                                <button type="submit">Confirm change</button>
                            </div>
                        </form>
                    </div>
                `;

    container.appendChild(card);
};

//event listener za upravljanje korisnicima
document.getElementById("userManagement")
    .addEventListener("click", userManager);

//event listener za upravljanje cijenom subskripcija
document.getElementById("paymentManagement")
    .addEventListener("click", subscriptionManager);

//logika za aktivni tab
const toolbar = document.getElementById("toolbar");

toolbar.addEventListener("click", function (e) {
    const button = e.target.closest("button");
    if (!button) return;

    //micemo klasu aktivno sa svih buttona
    toolbar.querySelectorAll("button")
        .forEach(btn => btn.classList.remove("active"));

    //dodajemo klasu aktivno pritisnutom dugmu
    button.classList.add("active");
});

//logika za učitavanje stranice
document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#toolbar button")?.click();
});

