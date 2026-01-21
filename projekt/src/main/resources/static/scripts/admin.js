            //funkcija za prikaz korisnika
            function userManager() {

                //hvatamo container
                const container = document.querySelector(".dynamicContainer");

                //prvo trebamo ubiti svu djecu u kontejneru
                container.innerHTML = '';

                //u container doajemo kartice s korisnicima
                const korisnici = [
                    {
                        ime: "Damjan Mamula",
                        uloga: "dog owner"
                    },
                    {
                        ime: "Jakov Andrić",
                        uloga: "dog walker"
                    }
                ];

                korisnici.forEach(korisnik => {
                    const card = document.createElement("div");
                    card.className = "korisnik-card";

                    card.innerHTML = `
                        <div>
                            <div class="user-flag">
                                <h3>${korisnik.ime}</h3>
                            </div>
                            <div class="user-box">
                                <p>Role: ${korisnik.uloga}</p>
                            </div>
                            <div class="deleteDugme">
                                <button class="deleteButton" data-ime="${korisnik.ime}">Delete user</button>
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
                        cijenaMjesec: "10 €",
                        cijenaGod: "100 €"
                    }
                ];

                card.innerHTML = `
                    <div>
                        <div class = "title-card">
                            <h3>Change prices</h3>
                        </div>
                        <div class="oldBox">
                            <p>Current monthly price: ${trenutno[0].cijenaMjesec}</p>
                            <p>Current yearly price: ${trenutno[0].cijenaGod}</p>
                        </div>
                        <form class = "newBox">
                            <label>
                                <p>New price(month): </p>
                                <input type = "number" min = "0" step = "0.01" placeholder = "0.00" />
                            </label>

                            <label>
                                <p>New price(year): </p>
                                <input type = "number" min = "0" step = "0.01" placeholder = "0.00" />
                            </label>
                        </form>
                        <div class="bttnWrap">
                            <button type="submit">Confirm change</button>
                        </div>
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

