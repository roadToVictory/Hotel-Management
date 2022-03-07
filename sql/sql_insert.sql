set search_path to hotel;

INSERT INTO osoba(imie, nazwisko, email, numer_dowodu, telefon) VALUES
('Adam', 'Poniedzielski', 'aponiedzielski@gmail.com', 'ABC654321','123123123'),
('Mateusz', 'Morawa', 'morawamateusz@gmail.com', 'AJE840715', '987997866'),
('Julia', 'Kucharczyk', 'j.kucharczyk@wp.pl', 'CEL184700', null),
('Beata', 'Cygan', 'cygan_beata@interia.pl', 'ZYX638421', '543216789');

INSERT INTO adres(id_osoba, kod_pocztowy, miejscowosc, ulica, numer) VALUES
(1, '00-025', 'Warszawa', 'Mazowiecka', '117/19'),
(2, '34-309', 'Rzeszow', 'Rejtana', '88'),
(3, '80-007', 'Gdansk', 'Morska', '69c'),
(4, '87-118', 'Torun', 'Ojcowska', '21/2');

INSERT INTO lista_gosci(imie, nazwisko, id_osoba) VALUES
('Tymoteusz', 'Abacki', 1),
('Filemon', 'Kucharczyk', 3),
('Bonifacy', 'Kucharczyk', 3);

INSERT INTO wyzywienie(id_osoba, kategoria, cena) VALUES
(1, 4, 120),
(1, 4, 120), 
(2, 2, 70),
(3, 5, 0),
(3, 5, 0),
(3, 5, 0),
(4, 1, 30);

INSERT INTO termin(termin_poczatkowy, termin_koncowy) VALUES
('2022-02-01', '2022-02-15'),
('2022-01-31', '2022-02-27'),
('2022-01-30', '2022-02-11'),
('2022-02-18', '2022-03-10'),
('2022-01-23', '2022-01-25');

INSERT INTO kategoria(cena, opis_kategorii, liczba_pokoi) VALUES
(250, '1 podwojne lozko', 2),
(200, '1 lozko', 4),
(270, '1 podwojne lozko, 1 dzieciece', 1),
(300, '2 lozka', 1),
(400, '3 lozka', 1);

INSERT INTO rezerwacja(id_osoba, id_kategoria, id_termin) VALUES
(1, 1, 2),
(2, 4, 4),
(3, 5, 1),
(3, 1, 5),
(4, 2, 3);

INSERT INTO sprzatanie(czy_codziennie, cena, id_rezerwacja) VALUES
(true, 40, 1),
(true, 40, 2),
(false, 100, 3),
(false, 100, 4),
(true, 40, 5);

INSERT INTO dodatkowa_oplata(id_rezerwacja, opis, oplata) VALUES
(1, 'kara za palenie w pokoju', 1000),
(2, 'oplata za korzystanie z hotelowego barku', 100),
(5, 'kara za wprowadzenie na teren hotelu kota', 800);


INSERT INTO pokoj(id_kategoria) VALUES
(1),
(1),
(2),
(2),
(2),
(2),
(3),
(4),
(5);

INSERT INTO termin_pokoj(id_termin, numer) VALUES
(1,9),
(2,1),
(3,3),
(4,8),
(5,2);

INSERT INTO pracownicy(imie, nazwisko, login, haslo) VALUES
('admin', 'admin', 'admin', 'admin'),
('Adam', 'Abacki', 'abacki', 'abackia');

