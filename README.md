Copyright - 2023 Stamatin Teodor 325 CA
# Proiect GlobalWaves  - Etapa 2

<div align="center"><img src="https://media1.tenor.com/m/epNMHGvRyHcAAAAd/gigachad-chad.gif" width="300px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa1](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa1)

## Notes:

Am folosit scheletul de cod oficial, de la tema 1.

Folosire generative AI:
-functii helper pentru functia printCurrentPage, din clasa User (ultimele)
-functie de verificare a datei daca este valida pentru un eveniment din clasa Artist
-functia de sortare a artistilor
-Strategy Pattern pentru functia getAllUsers
-copilot pentru javaDocs

## Descriere proiect:

Pentru functia de switchConnection am apelat o functie din clasa Admin, pentru a putea verifica
daca userul exista, si daca este user/artist/host. Daca este user normal, se apeleaza functia de
switch connection din clasa User, care schimba conexiunea userului. De asemenea, la unele comenzi
care necesita conexiune, am adaugat un if pentru a verifica daca userul este conectat sau nu.

La addUser am folosit un switch case pentru a determina tipul de user nou, si am apleat functii din
clasa Admin pentru fiecare tip, care adauga userul in lista de useri respectiva.

La addAlbum si addPodcast fac ceva similar, adica apelez functii din Admin si verific daca pot
adauga albumul/podcastul in lista userului respectiv.

Pentru showAlbums si showPodcasts, am returnat o lista de bodyuri jackson cu albumele/podcasturile
din clasa Admin.

La printCurrentPage am luat userul din lista de useri si am apelat o functie din clasa User.Aceasta
verifica cu un switch case unde am verificat daca pagina curenta a userului este home sau
likedsongs. Daca nu este niciuna din cele 2 de mai sus atunci intra pe cazul default ceea ce
inseamna ca numele paginii curente este ori numele unui artist ori numele unui host. Verific ce fel
de entitate este iar apoi ma ajut de functii helper pentru a afisa datele necesare.

Functiile addMerch, addEvent, addAnnouncement fac toate ceva asemanator. Apelez functia din clasa 
Admin pentru a verifica corectitudinea userului iar apoi apelez functiile din clasele Artist si
Host. Adaug obiectul in lista de obiecte a userului respectiv. Pentru un event am facut o functie
helper (validDate) care verifica daca data este valida sau nu.

La removeMerch, removeEvent, removeAnnouncement fac ceva asemanator cu functiile de mai sus. Apelez
functiile, dar in loc sa adaug in lista, le dau remove.

La removeAlbum si removePodcast, inainte de a sterge instanta respectiva, verific mai intai daca
poate fi stearsa, adica daca este cumva loaded intr-un player.

Pentru changePage am folosit Factory Pattern. Am creat o interfata numita Page, clasa abstracta si
2 clase care implementeaza interfata. Clasa ChangePageFactory e clasa care face obiectele de tipul
Page in functie de tipul primit. Metoda createPage este statica si primeste ca parametri numele
userului si tipul paginii, si returneaza un obiect de tipul Page.

La getAllUsers am folosit Strategy Pattern. Am creat un package nou numit getAllUsers.Strategy, si
o interfata numita GetAllUsersStrategy. Am o clasa de context numita GetUsers care utilizeaza o
lista strategies pentru a obtine toti utilizatorii. Functia getAllUsers parcurge fiecare din
strategiile din lista si apeleaza functia getAllUsers specifica fiecarei strategii. Rezultatele
sunt intoarse intr-o lista de useri. GetAllUsers, GetAllArtists, GetAllHosts obtin toti userii din
Admin si ii transforma in liste.

Pentru deleteUser, iau usernameul si apelez functia din Admin, ce cauta fiecare tip de user (host,
artist, user normal) si verifica daca il poate sterge. Verifica playerul, playlisturile, albumele
si podcasturile, si daca il poate sterge, face si modificarile necesare.

De asemenea, a trebuit sa schimb functionalitatea functiilor search si select pentru a putea
prelucra albume si pageuri. Am adaugat albumul la functionalitatea initiala, si am creat clase
auxiliare pentru a putea prelucra si paginile ( FilterUtilsPage, SearchBarPage, FiltersPage ).

<div align="center"><img src="https://media1.tenor.com/m/H6SbjeOHneYAAAAd/please-be-easy-on-me-johnny-middlebrooks.gif" width="300px"></div>






