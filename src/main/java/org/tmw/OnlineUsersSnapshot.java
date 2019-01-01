package org.tmw;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OnlineUsersSnapshot {

    static final List<String> PATEAME_BOTS = Arrays.asList(
            "Corson", "Bukavac", "playerone", "Aamon", "Abezethibou",
            "Abraxax", "Abyzou", "Adrammelech", "Aeshma", "Agaliarept",
            "Agrat", "Agiel", "Haborym", "Alloces", "Allu", "Amaymon",
            "Amdusias", "Anammelech", "Ancitif", "Armaros", "Andhaka",
            "Andrealphus", "Anzu", "Arunasura", "Asag", "Asakku", "Barong",
            "Bael", "Bakasura", "Baku", "Balberith", "Bali", "Barbas",
            "Barbatos", "Bathin", "Beleth", "Berith", "Bifrons", "Botis",
            "Buer", "Bune", "Bushyasta", "Chemosh", "Cimejes", "Crocell",
            "Culsu", "Daeva", "Dajjal", "Dantalion", "Danjal", "Decarabia",
            "Demiurge", "Drekavac", "Dzoavits", "Eblis", "Eisheth", "Eligos",
            "Foras", "Forcas", "Forras", "Forneus", "Furcas", "Caprice",
            "Gaap", "Gaderel", "Gaki", "Gamigin", "Glasya", "Gremory",
            "Grigori", "Gualichu", "Guayota", "Haagenti", "Hauras", "Haures",
            "Havres", "Hinn", "Ipos", "Jikininki", "Kabandha", "Kasadya",
            "Kroni", "Killakee", "Kukudh", "Kumbhakarna", "Lechies", "Leyak",
            "Lempo", "Leraje", "Leraie", "Lilin", "Ljubi", "Lucifuge",
            "Maricha", "Morax", "Masih", "Marchosias", "Mastema", "Merihem",
            "Murmur", "Naamah", "Naberus", "Ninurta", "Namtar", "Onoskelis",
            "Orias", "Oriax", "Orobas", "Ordog", "Otokata", "Paimon", "Pelesit",
            "Phenex", "Penemue", "Pithius", "Pocong", "Pontianak", "Preta",
            "Pruflas", "Puloman", "Rahab", "Ronove", "Rusalka", "Rakshasa",
            "Rangda", "Sabnock", "Saleos", "Seir", "Semyaza", "Shedim",
            "Sitri", "Sthenno", "Stihi", "Stolas", "Suanggi", "Surgat",
            "Titivillus", "Toyol", "Tuchulcha", "Ziminiar", "Valac", "Valefar",
            "Vapula", "Vassago", "Vepar", "Vine", "Wechuge", "Yeqon", "Zagan",
            "Zepar");

    static final List<String> USERS_TRADE_BOTS = Arrays.asList(
            "Skel-Mart", "Supply Store");

    static final List<String> PASSIVE_USERS = Arrays.asList(
            "Hyperspace");


    private List<String> users;

    private LocalDateTime dateTime;

    public OnlineUsersSnapshot removeBots() {
        this.getUsers().removeAll(USERS_TRADE_BOTS);
        this.getUsers().removeAll(PASSIVE_USERS);
        return this;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(this.dateTime)
                .append("  ")
                .append(this.users)
                .toString();
    }
}
