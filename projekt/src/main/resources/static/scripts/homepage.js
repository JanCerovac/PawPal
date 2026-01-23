const DEFAULT_WALKER = window.DEFAULT_WALKER;

//get role from database
let role = window.role

if (role == "OWNER") {
    // TODO: add subscribed to database
    // check in database if owner is subscribed to recieve info about new walkers
    let subscribed = false;
}

window.addEventListener("load", function () {
    function hideElementsByClass(className) {
        const elements = document.getElementsByClassName(className);
        for (let el of elements) {
            el.style.display = "none";
        }
    }

    function showElementsByClass(className) {
        const elements = document.getElementsByClassName(className);
        for (let el of elements) {
            el.style.display = "";
        }
    }

    function hideAll() {
        hideElementsByClass("walkerPhotos");
        hideElementsByClass("walkerData");
        hideElementsByClass("dogData")
        hideElementsByClass("dogContainer");
        hideElementsByClass("appointment");
        hideElementsByClass("availableAppointments");
        hideElementsByClass("review");
        hideElementsByClass("reviewBlock");
        hideElementsByClass("manageAccount");
        hideElementsByClass("subscription");
        hideElementsByClass("filterWalkers");
        hideElementsByClass("filter");
        hideElementsByClass("walkerCard");
        hideElementsByClass("walkerContainer");
        hideElementsByClass("walker");
        hideElementsByClass("owner");
    }

    function showPhotos(photos) {
        document.getElementById("walkerPhotos").innerHTML = "";

        photos.forEach(photo => {
            document.getElementById("walkerPhotos").innerHTML += photo;
        });
    }

    function showWalkerData(walker) {
        // pick JS walker if provided, otherwise Thymeleaf default
        const w = walker ?? DEFAULT_WALKER;
        if (!w) return;

        if (w.rating == null || w.rating == "") {
            document.getElementById("starRate").innerHTML = "<img src='/images/starRate.svg'>" + "Not rated";
        } else {
            document.getElementById("starRate").innerHTML = "<img src='/images/starRate.svg'>" + walkerData.rating;
        }
        document.getElementById("name").innerHTML = "Name: " + w.name;
        document.getElementById("surname").innerHTML = "Surname: " + w.surname;
        document.getElementById("contact").innerHTML = "Contact: " + w.contact;
        document.getElementById("location").innerHTML = "Location: " + w.location;

        if (role === "OWNER") {
            document.getElementById("edit").style.display = "none";
        } else {
            document.getElementById("edit").style.display = "";
        }
    }

    function showWalkerProfile(walker) {
        // TODO: implement photos
        //check if walker has profile photos
        // let hasPhotos = true;
        // if (hasPhotos) {
        //     //get profile photos from database as a array
        //     let photos = [
        //         "<img src='/images/dogWalker.webp'>"
        //     ];
        //     showPhotos(photos);
        //     showElementsByClass("walkerPhotos");
        // }

        showWalkerData(walker);
        showElementsByClass("walkerData");

        //get appointments from database
        const appointments = (window.walks || []).map(parseWalkDetails)

        document.querySelector(".appointmentContainer").innerHTML = "";
        appointments.forEach(addAppointmentCard);
        showElementsByClass("availableAppointments");
        document.getElementById("setAppointment").style.display = "none";

        // TODO: implement reviews
        //get reviews from database
        // const reviews = [
        //     {
        //         grade: "5",
        //         comment: "Najbolji šetač ikad!",
        //         image: "<img src='/images/dogWalker.webp'>"
        //     },
        //     {
        //         grade: "5",
        //         comment: "Moram se složiti s prethodnim komentarima!",
        //         image: "null"
        //     }
        // ];
        // document.querySelector(".reviewBlock").innerHTML = "";
        // reviews.forEach(addReviewCard);
        // showElementsByClass("reviewBlock");

        showElementsByClass("manageAccount");
    }

    function addReviewCard(review) {
        const template = document.querySelector(".review");
        const clone = template.cloneNode(true);

        clone.classList.remove("template");
        clone.style.display = "";

        clone.querySelector(".grade").innerHTML = "<img src=/images/starRate.svg>" + review.grade;
        clone.querySelector(".comment").innerHTML = review.comment;
        if (!(review.image === "null")) {
            clone.querySelector(".image").innerHTML = review.image;
        } else {
            clone.querySelector(".image").style.display = "none";
        }

        document.querySelector(".reviewBlock").insertAdjacentElement("afterbegin", clone);
    }

    function addWalkerCard(walkerCard) {
        const template = document.querySelector(".walkerCard");
        const clone = template.cloneNode(true);

        clone.classList.remove("template");
        clone.style.display = "";

        clone.querySelector(".grade").innerHTML = "<img src=/images/starRate.svg>" + walkerCard.starRate;
        clone.querySelector(".fullName").innerHTML = "Full name: " + walkerCard.name + " " + walkerCard.surname;
        clone.querySelector(".location").innerHTML = "Location: " + walkerCard.location;
        clone.querySelector(".contact").innerHTML = "Contact: " + walkerCard.contact;

        document.querySelector(".walkerContainer").insertAdjacentElement("beforeend", clone);
    }

    function addAppointmentCard(appointment) {
        const template = document.querySelector(".appointment");
        const clone = template.cloneNode(true);

        clone.classList.remove("template");
        clone.style.display = "";

        clone.dataset.eventId = appointment.id
        clone.querySelector(".type").innerHTML = "Type: " + appointment.type.toLowerCase().replace(/^\w/, c => c.toUpperCase()) + (appointment.reserved ? " (RESERVED)" : "");
        clone.querySelector(".price").innerHTML = "Price: " + appointment.price + " €";
        clone.querySelector(".duration").innerHTML = "Duration: " + appointment.duration;
        clone.querySelector(".datetime").innerHTML = "Time: " + appointment.datetime

        if (role === "WALKER") {
            clone.querySelector(".manageAppointment").style.display = "";
            clone.querySelector(".book").style.display = "none";
        } else {
            clone.querySelector(".manageAppointment").style.display = "none";
            clone.querySelector(".book").style.display = "";
        }

        document.querySelector(".appointmentContainer").insertAdjacentElement("afterbegin", clone);
    }

    function addDogCard(dog) {
        const template = document.querySelector(".dogData");
        const clone = template.cloneNode(true);

        clone.classList.remove("template");
        clone.style.display = "block";

        clone.dataset.dogId = dog.id
        clone.querySelector(".name").innerHTML = "Name: " + dog.name;
        clone.querySelector(".breed").innerHTML = "Breed: " + dog.breed;
        clone.querySelector(".age").innerHTML = "Age: " + dog.age;
        clone.querySelector(".energyLevel").innerHTML = "Energy level: " + dog.energyLevel;
        clone.querySelector(".allowedTreats").innerHTML = "Allowed treats: " + dog.allowedTreats;
        clone.querySelector(".healthcare").innerHTML = "Healthcare: " + dog.healthcare;
        clone.querySelector(".personality").innerHTML = "Personality: " + dog.personality;

        document.querySelector(".dogContainer").insertAdjacentElement("beforeend", clone);
    }

    function afterLogin() {
        hideAll();
        showElementsByClass("manageAccount");

        if (role === "WALKER") {
            showWalkerProfile();

            document.getElementById("addDog").style.display = "none";
            document.getElementById("seeOtherWalkers").style.display = "none";

            showElementsByClass("walker");

        } else {

            //get dogs info from database
            const dogs = window.dogs

            document.querySelector(".dogContainer").innerHTML = "";
            dogs.forEach(addDogCard);
            showElementsByClass("dogContainer");

            showElementsByClass("manageAccount");
            document.getElementById("seeOtherWalkers").style.display = "none";
            document.getElementById("delete").style.display = "";
            document.getElementById("addDog").style.display = "";
            document.getElementById("logOut").style.display = "";

            showElementsByClass("owner");

            let whereIsOwner = "account";
        }
    }

    function recommendedWalkers() {
        whereIsOwner = "recommendedWalkers";

        hideAll();

        showElementsByClass("subscription");
        showElementsByClass("owner");

        const text = " You can change it by clicking the button.";

        if (!subscribed) {
            const el = document.getElementById("subscribed");
            el.innerHTML = "You are not subscribed to recieving information about new walkers." + text;
            document.getElementById("no").style.display = "";
            document.getElementById("yes").style.display = "none";

        } else {
            const el = document.getElementById("subscribed");
            el.innerHTML = "You are subscribed to recieving information about new walkers." + text;
            document.getElementById("yes").style.display = "";
            document.getElementById("no").style.display = "none";

            // TODO: implement this
            // get info about walkers that registred in last 24 hours from database
            // or last n walkers
            const newWalkers = window.walkers;
            document.querySelector(".walkerContainer").innerHTML = "";
            newWalkers.forEach(addWalkerCard);
            showElementsByClass("walkerContainer");
        }
    }

    function findWalker(walkers) {
        whereIsOwner = "findWalker";

        //IMPORTANT!
        //get info about selected walkers from database
        //information about filter given in form
        const filteredWalkers = walkers;
        document.querySelector(".walkerContainer").innerHTML = "";
        filteredWalkers.forEach(addWalkerCard);
        showElementsByClass("walkerContainer");
    };

    function showWalkerToOwner(cardEl) {
        hideAll();

        // Extract values FROM THE CARD (not from .walkerData)
        const starRateText = (cardEl.querySelector(".grade")?.textContent || "").trim();

        const fullNameText = (cardEl.querySelector(".fullName")?.textContent || "").trim();
        // expected "Full name: John Doe"
        const fullName = fullNameText.includes(":")
            ? fullNameText.split(":").slice(1).join(":").trim()
            : fullNameText;

        const parts = fullName.split(" ");
        const name = parts.shift() || "";
        const surname = parts.join(" ") || "";

        const locationText = (cardEl.querySelector(".location")?.textContent || "").trim();
        const location = locationText.includes(":")
            ? locationText.split(":").slice(1).join(":").trim()
            : locationText;

        const contactText = (cardEl.querySelector(".contact")?.textContent || "").trim();
        const contact = contactText.includes(":")
            ? contactText.split(":").slice(1).join(":").trim()
            : contactText;

        const extractedWalker = {
            starRate: starRateText || "Not rated",
            name,
            surname,
            location,
            contact
        };

        console.log("Clicked walker:", extractedWalker);

        // Show profile for that walker
        showWalkerProfile(extractedWalker);

        document.getElementById("add").style.display = "none";
        document.getElementById("addDog").style.display = "none";
        document.getElementById("logOut").style.display = "none";
        document.getElementById("delete").style.display = "none";
        document.getElementById("seeOtherWalkers").style.display = "";

        showElementsByClass("owner");
    }

    function walkButtonFunc() {
        hideAll(); //skrivam sve elemente
        showElementsByClass("walkerContainer"); //otvaram ovaj walker container
        showElementsByClass("owner");

        //sada u njega dodajemo kartice sa setacima
        const container = document.querySelector(".walkerContainer");
        container.innerHTML = ''

        const termini = window.events;

        termini.forEach(termin => {
            if ((termin.description || "").includes("REZERVACIJA")) return;

            const card = document.createElement("div");
            card.className = "termin-card";

            const result = {};
            const text = (termin.description || "").replace(/\r\n/g, "\n").trim();
            const NL = "\\r?\\n"; // matches \n or \r\n

            const walkerMatch = text.match(new RegExp(`Walker:\\s*(.*?)\\s*${NL}\\s*Walker Username:`, "i"));
            const usernameMatch = text.match(new RegExp(`Walker Username:\\s*(.*?)\\s*${NL}\\s*Location:`, "i"));
            const locationMatch = text.match(new RegExp(`Location:\\s*(.*?)\\s*${NL}\\s*Type:`, "i"));
            const typeMatch = text.match(new RegExp(`Type:\\s*(.*?)\\s*${NL}\\s*Price:`, "i"));
            const priceMatch = text.match(new RegExp(`Price:\\s*(.*?)\\s*${NL}\\s*Duration:`, "i"));
            const durationMatch = text.match(/Duration:\s*(\d+)\s*minutes/i);

            result.name = walkerMatch?.[1]?.trim() ?? "";
            result.tip = (typeMatch?.[1]?.trim() ?? "").toLowerCase().replace(/^\w/, c => c.toUpperCase());
            result.cijena = priceMatch?.[1]?.trim() ?? "";
            result.trajanje = durationMatch?.[1]?.trim() ?? "";
            result.username = usernameMatch?.[1]?.trim() ?? "";
            result.location = locationMatch?.[1]?.trim() ?? "";
            result.start = new Date(termin.start.dateTime.value).toLocaleString()

            card.innerHTML = `
                        <div>
                            <div class="termin-flag">
                                <h3>${result.name}</h3>
                            </div>
                            <div class="termin-box">
                                <p>Location: ${result.location}</p>
                                <p>Type of walk: ${result.tip}</p>
                                <p>Price: ${result.cijena} €</p>
                                <p>Time: ${result.start}</p>
                                <p>Duration: ${result.trajanje} minutes</p>
                            </div>
                            <div class="rezervacijaDugme">
                                <button class="rezervBttn"
                                 data-ime="${result.name}"
                                 data-id="${termin.id}"
                                 data-username="${result.username}"
                                 data-date="${result.start}"
                                 data-duration="${result.trajanje}"
                                 data-type="${result.tip}"
                                 >Reserve</button>
                            </div>
                        </div>
                    `;

            container.appendChild(card);
        });
    };

    // funkcija za generiranje payment contenta
    function paymentFunc() {

        //skrivam suvisne elemente i prikazujem potrebne
        hideAll();
        showElementsByClass("walker");
        showElementsByClass("walkerContainer");

        //dohvacam kontejner
        const container2 = document.querySelector(".walkerContainer");
        container2.innerHTML = '';

        //kreiram karticu za placanje
        const pay = document.createElement("div");
        pay.className = "pay-card";

        const trenutno = [
            {
                cijenaMjesec: window.price.monthly,
                cijenaGod: window.price.yearly
            }
        ];

        pay.innerHTML = `
                    <div>
                        <div class = "title-card">
                            <h3>Pay subscription</h3>
                        </div>
                        <div class="priceBox">
                            <p>Monthly price: ${trenutno[0].cijenaMjesec} €</p>
                            <p>Yearly price: ${trenutno[0].cijenaGod} €</p>
                        </div>
                        <form>
                            <div class="choose_role">
                                <div class="text_role">Pay:</div>
                                <input type="radio" name="role" id="owner" checked>
                                <label for="owner">Monthly</label>
                                <input type="radio" name="role" id="walker">
                                <label for="walker">Yearly</label>
                            </div>
                            <div class="reg_bttn_wrap">
                                <div class="reg_button">
                                    <p><input type="submit" value="Submit"></p>
                                </div>
                            </div>
                        </form>
                    </div>
                `;

        container2.appendChild(pay);
    };

    afterLogin();

    document.getElementById("delete").addEventListener("click", function () {
        const choice = confirm("Your account will be permanently deleted and you won't be able to undo it!");
        if (choice) {
            alert("Your account has been successfully deleted. You will be redirected to the login page.");

            fetch("/delete", {
                method: "POST"
            }).then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                }
            });
        }
    });

    document.getElementById("edit").addEventListener("click", function () {

    });

    document.getElementById("addDog").addEventListener("click", function () {
        // redirect to setup page
        fetch("/edit/owner/add")
            .then(res => res.text())
            .then(html => {
                document.open();
                document.write(html);
                document.close();
            });
    });

    document.getElementById("seeOtherWalkers").addEventListener("click", function () {
        if (whereIsOwner == "recommendedWalkers") recommendedWalkers();
        if (whereIsOwner == "findWalker") {
            hideAll();
            showElementsByClass("filterWalkers");
            showElementsByClass("owner");
            findWalker();
        }
    });

    document.body.addEventListener("click", function (e) {
        const card = e.target.closest(".walkerCard");
        if (!card) return;

        // ignore the hidden template card if you have one
        if (card.classList.contains("template")) return;

        showWalkerToOwner(card);
    });

    document.body.addEventListener("click", function (e) {
        if (e.target.closest(".edit")) {
            let edit = e.target.closest(".edit");
            if (edit.closest(".manageDog")) {
                const dogDataEl = document.querySelector(".dogData");

                const getValueAfterColon = (selector) => {
                    const el = dogDataEl.querySelector(selector);
                    if (!el) return "";
                    const text = el.textContent.trim();
                    const idx = text.indexOf(":");
                    return idx >= 0 ? text.slice(idx + 1).trim() : "";
                };

                const params = new URLSearchParams({
                    id: dogDataEl.dataset.dogId,
                    name: getValueAfterColon(".name"),
                    breed: getValueAfterColon(".breed"),
                    age: getValueAfterColon(".age"),
                    energyLevel: getValueAfterColon(".energyLevel"),
                    allowedTreats: getValueAfterColon(".allowedTreats"),
                    healthcare: getValueAfterColon(".healthcare"),
                    personality: getValueAfterColon(".personality")
                });

                // redirect to setup page
                window.location.href = `/edit/owner/edit?${params.toString()}`;
            } else {
                // edit appointment
                edit.closest(".appointment").style.display = "none"
                document.getElementById("setAppointment").style.display = "";
                //change info in database about appointment
            }
        }

        if (e.target.closest(".delete")) {
            let remove = e.target.closest(".delete");
            if (role == "OWNER") {
                const choice = confirm("This dog will be permanently removed and you won't be able to undo it!");
                if (choice) {
                    alert("Selected dog has been successfully removed.");

                    const dogDataEl = document.querySelector(".dogData");

                    const getValueAfterColon = (selector) => {
                        const el = dogDataEl.querySelector(selector);
                        if (!el) return "";
                        const text = el.textContent.trim();
                        const idx = text.indexOf(":");
                        return idx >= 0 ? text.slice(idx + 1).trim() : "";
                    };

                    const params = new URLSearchParams({
                        name: getValueAfterColon(".name"),
                        breed: getValueAfterColon(".breed"),
                        age: getValueAfterColon(".age"),
                        energyLevel: getValueAfterColon(".energyLevel"),
                        allowedTreats: getValueAfterColon(".allowedTreats"),
                        healthcare: getValueAfterColon(".healthcare"),
                        personality: getValueAfterColon(".personality")
                    });

                    fetch("/edit/owner/delete", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: params.toString()
                    })
                        .then(() => window.location.reload())
                }
            } else {
                const choice = confirm("This appointment will be permanently removed and you won't be able to undo it!");
                if (choice) {
                    alert("Selected appointment has been successfully removed.");

                    const appointmentEl = e.target.closest(".appointment");
                    const eventId = appointmentEl.dataset.eventId;

                    console.log("Deleting appointment id:", eventId);

                    fetch("/walk/walker/delete", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: `id=${encodeURIComponent(eventId)}`
                    }).then(() => window.location.reload());
                }
            }
        }

        if (e.target.closest(".account")) {
            afterLogin();
        }

        if (e.target.closest(".walk")) {
            // redirect to walk page
            walkButtonFunc();
        }
    });

    document.getElementById("recommendedWalks").addEventListener("click", function () {
        // redirect to walk page
        window.location.href = "walk.html";
    });

    document.getElementById("paymentMethod")
        .addEventListener("click", paymentFunc);

    document.getElementById("findWalker").addEventListener("click", function () {
        hideAll();
        showElementsByClass("filterWalkers");
        showElementsByClass("owner");

        let filterChosen = false;
        let filterBy = null;
    });

    document.getElementById("recommendedWalkers").addEventListener("click", recommendedWalkers);

    document.getElementById("yes").addEventListener("click", function () {
        subscribed = false;
        recommendedWalkers();
    });

    document.getElementById("no").addEventListener("click", function () {
        subscribed = true;
        recommendedWalkers();
    });

    document.getElementById("add").addEventListener("click", function () {
        // form for new appointment
        document.getElementById("setAppointment").style.display = "";
    });

    document.getElementById("submit").addEventListener("click", function () {
        //!IMPORTANT
        //form for new appointment is submitted
        //save it in database
        document.getElementById("setAppointment").style.display = "none";
    });

    document.getElementById("filterByLocation").addEventListener("click", function () {
        showElementsByClass("filter");
        document.getElementById("walkerLocation").style.display = "";
        document.getElementById("walkerPrice").style.display = "none";
        document.getElementById("walkerGrade").style.display = "none";
        filterBy = "location";
    });

    document.getElementById("filterByPrice").addEventListener("click", function () {
        showElementsByClass("filter");
        document.getElementById("walkerPrice").style.display = "";
        document.getElementById("walkerLocation").style.display = "none";
        document.getElementById("walkerGrade").style.display = "none";
        filterBy = "price";
    });

    document.getElementById("filterByGrade").addEventListener("click", function () {
        showElementsByClass("filter");
        document.getElementById("walkerGrade").style.display = "";
        document.getElementById("walkerLocation").style.display = "none";
        document.getElementById("walkerPrice").style.display = "none";
        filterBy = "grade";
    });

    document.getElementById("defineFilter").addEventListener("submit", async (e) => {
        e.preventDefault(); // stops page reload

        const form = e.target;

        const payload = {
            walkerLocation: form.walkerLocation.value,
            walkerPrice: form.walkerPrice.value ? Number(form.walkerPrice.value) : null,
            walkerGrade: form.walkerGrade.value ? Number(form.walkerGrade.value) : null
        };

        const res = await fetch("/search", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            console.error("Search failed", await res.text());
            return;
        }

        const walkers = await res.json();
        console.log(walkers)

        findWalker(walkers)
    });

    // document.getElementById("searchByFilter").addEventListener("click", function () {
    //     //!IMPORTANT
    //     //form for filtering walkers was submitted
    //     //filtered data will be needed in function findWalker
    //     filterChosen = true;
    //     findWalker();
    // });

    const popup = document.getElementById("rezervPop");
    const closeBtn = popup.querySelector(".close");
    const setacText = document.getElementById("imeSetac");

    document.addEventListener("click", (e) => {
        if (e.target.classList.contains("rezervBttn")) {
            const ime = e.target.dataset.ime;
            setacText.textContent = `Walker: ${ime}`;
            popup.classList.remove("hidden");

            document.getElementById("_eventId").value = e.target.dataset.id
            document.getElementById("_usernameWalker").value = e.target.dataset.username

            document.getElementById("_date").value = e.target.dataset.date
            document.getElementById("_duration").value = e.target.dataset.duration
            document.getElementById("_type").value = e.target.dataset.type

            console.log(e.target.dataset.username)
        }
    });

    closeBtn.addEventListener("click", () => {
        popup.classList.add("hidden");
    });

    popup.addEventListener("click", (e) => {
        if (e.target === popup) {
            popup.classList.add("hidden");
        }
    });

    //popUp za uspjesnu rezervaciju
    const understood = document.getElementById("understood");
    const popup2 = document.getElementById("uSetnjiPop");

    understood.addEventListener("click", () => {
        popup2.classList.add("hidden");
    });

    function parseWalkDetails(event) {
        const desc = event.description || "";
        console.log(event.start.dateTime)

        const get = (label) => {
            const match = desc.match(new RegExp(`${label}:\\s*(.+)`));
            return match ? match[1].trim() : null;
        };

        return {
            id: event.id,
            type: get("Type"),
            price: get("Price") ? Number(get("Price")) : null,
            duration: get("Duration") ? String(get("Duration")) : null,
            datetime: (new Date(event.start.dateTime.value)).toLocaleString(),
            reserved: desc.includes("REZERVACIJA")
        };
    }

    const button1 = document.querySelector(".walking");
    const button2 = document.getElementById("recommendedWalks");

    button1.classList.add("skriven");
    button2.classList.add("skriven");
})