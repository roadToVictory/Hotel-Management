CREATE SCHEMA hotel;

CREATE SEQUENCE hotel.pracownicy_id_pracownik_seq;

CREATE TABLE hotel.Pracownicy (
                id_pracownik INTEGER NOT NULL DEFAULT nextval('hotel.pracownicy_id_pracownik_seq'),
                imie VARCHAR(20) NOT NULL,
                nazwisko VARCHAR(30) NOT NULL,
                login VARCHAR(20) NOT NULL UNIQUE,
                haslo VARCHAR(20) NOT NULL,
                CONSTRAINT pracownicy_pk PRIMARY KEY (id_pracownik)
);


ALTER SEQUENCE hotel.pracownicy_id_pracownik_seq OWNED BY hotel.Pracownicy.id_pracownik;

CREATE SEQUENCE hotel.kategoria_id_kategoria_seq;

CREATE TABLE hotel.kategoria (
                id_kategoria INTEGER NOT NULL DEFAULT nextval('hotel.kategoria_id_kategoria_seq'),
                cena DOUBLE PRECISION NOT NULL,
                opis_kategorii VARCHAR NOT NULL,
                liczba_pokoi INTEGER NOT NULL,
                CONSTRAINT kategoria_pk PRIMARY KEY (id_kategoria)
);


ALTER SEQUENCE hotel.kategoria_id_kategoria_seq OWNED BY hotel.kategoria.id_kategoria;

CREATE SEQUENCE hotel.pokoj_numer_seq;

CREATE TABLE hotel.pokoj (
                numer INTEGER NOT NULL DEFAULT nextval('hotel.pokoj_numer_seq'),
                id_kategoria INTEGER NOT NULL,
                CONSTRAINT pokoj_pk PRIMARY KEY (numer)
);


ALTER SEQUENCE hotel.pokoj_numer_seq OWNED BY hotel.pokoj.numer;

CREATE SEQUENCE hotel.termin_id_termin_seq;

CREATE TABLE hotel.termin (
                id_termin INTEGER NOT NULL DEFAULT nextval('hotel.termin_id_termin_seq'),
                termin_poczatkowy DATE NOT NULL,
                termin_koncowy DATE NOT NULL,
                CONSTRAINT termin_pk PRIMARY KEY (id_termin)
);


ALTER SEQUENCE hotel.termin_id_termin_seq OWNED BY hotel.termin.id_termin;

CREATE TABLE hotel.termin_pokoj (
                id_termin INTEGER NOT NULL,
                numer INTEGER NOT NULL,
                CONSTRAINT termin_pokoj_pk PRIMARY KEY (id_termin, numer)
);


CREATE SEQUENCE hotel.osoba_id_osoba_seq;

CREATE TABLE hotel.osoba (
                id_osoba INTEGER NOT NULL DEFAULT nextval('hotel.osoba_id_osoba_seq'),
                imie VARCHAR(20) NOT NULL,
                nazwisko VARCHAR(30) NOT NULL,
                email VARCHAR(40) NOT NULL,
                numer_dowodu VARCHAR(9) NOT NULL,
                telefon INTEGER,
                CONSTRAINT osoba_pk PRIMARY KEY (id_osoba)
);


ALTER SEQUENCE hotel.osoba_id_osoba_seq OWNED BY hotel.osoba.id_osoba;

CREATE SEQUENCE hotel.rezerwacja_id_rezerwacja_seq;

CREATE TABLE hotel.rezerwacja (
                id_rezerwacja INTEGER NOT NULL DEFAULT nextval('hotel.rezerwacja_id_rezerwacja_seq'),
                id_osoba INTEGER NOT NULL,
                id_kategoria INTEGER NOT NULL,
                id_termin INTEGER NOT NULL,
                CONSTRAINT rezerwacja_pk PRIMARY KEY (id_rezerwacja)
);


ALTER SEQUENCE hotel.rezerwacja_id_rezerwacja_seq OWNED BY hotel.rezerwacja.id_rezerwacja;

CREATE SEQUENCE hotel.dodatkowa_oplata_id_dodatkowa_oplata_seq;

CREATE TABLE hotel.dodatkowa_oplata (
                id_dodatkowa_oplata INTEGER NOT NULL DEFAULT nextval('hotel.dodatkowa_oplata_id_dodatkowa_oplata_seq'),
                id_rezerwacja INTEGER NOT NULL,
                opis VARCHAR,
                oplata DOUBLE PRECISION DEFAULT 0,
                CONSTRAINT dodatkowa_oplata_pk PRIMARY KEY (id_dodatkowa_oplata)
);


ALTER SEQUENCE hotel.dodatkowa_oplata_id_dodatkowa_oplata_seq OWNED BY hotel.dodatkowa_oplata.id_dodatkowa_oplata;

CREATE SEQUENCE hotel.sprzatanie_id_sprzatanie_seq;

CREATE TABLE hotel.sprzatanie (
                id_sprzatanie INTEGER NOT NULL DEFAULT nextval('hotel.sprzatanie_id_sprzatanie_seq'),
                czy_codziennie BOOLEAN NOT NULL,
                cena DOUBLE PRECISION NOT NULL,
                id_rezerwacja INTEGER NOT NULL,
                CONSTRAINT sprzatanie_pk PRIMARY KEY (id_sprzatanie)
);


ALTER SEQUENCE hotel.sprzatanie_id_sprzatanie_seq OWNED BY hotel.sprzatanie.id_sprzatanie;

CREATE SEQUENCE hotel.adres_id_adres_seq;

CREATE TABLE hotel.adres (
                id_adres INTEGER NOT NULL DEFAULT nextval('hotel.adres_id_adres_seq'),
                id_osoba INTEGER NOT NULL,
                kod_pocztowy VARCHAR(6) NOT NULL,
                miejscowosc VARCHAR(30) NOT NULL,
                ulica VARCHAR(30) NOT NULL,
                numer VARCHAR NOT NULL,
                CONSTRAINT adres_pk PRIMARY KEY (id_adres)
);


ALTER SEQUENCE hotel.adres_id_adres_seq OWNED BY hotel.adres.id_adres;

CREATE SEQUENCE hotel.lista_gosci_id_gosc_seq;

CREATE TABLE hotel.lista_gosci (
                id_gosc INTEGER NOT NULL DEFAULT nextval('hotel.lista_gosci_id_gosc_seq'),
                imie VARCHAR(20) NOT NULL,
                nazwisko VARCHAR(30) NOT NULL,
                id_osoba INTEGER NOT NULL,
                CONSTRAINT lista_gosci_pk PRIMARY KEY (id_gosc)
);


ALTER SEQUENCE hotel.lista_gosci_id_gosc_seq OWNED BY hotel.lista_gosci.id_gosc;

CREATE SEQUENCE hotel.wyzywienie_id_wyzywienie_seq;

CREATE TABLE hotel.wyzywienie (
                id_wyzywienie INTEGER NOT NULL DEFAULT nextval('hotel.wyzywienie_id_wyzywienie_seq'),
                id_osoba INTEGER NOT NULL,
                kategoria INTEGER DEFAULT 0 NOT NULL,
                cena DOUBLE PRECISION NOT NULL,
                CONSTRAINT wyzywienie_pk PRIMARY KEY (id_wyzywienie)
);


ALTER SEQUENCE hotel.wyzywienie_id_wyzywienie_seq OWNED BY hotel.wyzywienie.id_wyzywienie;

ALTER TABLE hotel.pokoj ADD CONSTRAINT kategoria_pokoj_fk
FOREIGN KEY (id_kategoria)
REFERENCES hotel.kategoria (id_kategoria)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.rezerwacja ADD CONSTRAINT kategoria_rezerwacja_fk
FOREIGN KEY (id_kategoria)
REFERENCES hotel.kategoria (id_kategoria)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.termin_pokoj ADD CONSTRAINT pokoj_termin_pokoj_fk
FOREIGN KEY (numer)
REFERENCES hotel.pokoj (numer)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.rezerwacja ADD CONSTRAINT termin_rezerwacja_fk
FOREIGN KEY (id_termin)
REFERENCES hotel.termin (id_termin)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.termin_pokoj ADD CONSTRAINT termin_termin_pokoj_fk
FOREIGN KEY (id_termin)
REFERENCES hotel.termin (id_termin)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.wyzywienie ADD CONSTRAINT osoba_wyzywienie_fk
FOREIGN KEY (id_osoba)
REFERENCES hotel.osoba (id_osoba)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.lista_gosci ADD CONSTRAINT osoba_lista_gosci_fk
FOREIGN KEY (id_osoba)
REFERENCES hotel.osoba (id_osoba)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.adres ADD CONSTRAINT osoba_adres_fk
FOREIGN KEY (id_osoba)
REFERENCES hotel.osoba (id_osoba)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.rezerwacja ADD CONSTRAINT osoba_rezerwacja_fk
FOREIGN KEY (id_osoba)
REFERENCES hotel.osoba (id_osoba)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.sprzatanie ADD CONSTRAINT rezerwacja_sprzatanie_fk
FOREIGN KEY (id_rezerwacja)
REFERENCES hotel.rezerwacja (id_rezerwacja)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE hotel.dodatkowa_oplata ADD CONSTRAINT rezerwacja_dodatkowa_oplata_fk
FOREIGN KEY (id_rezerwacja)
REFERENCES hotel.rezerwacja (id_rezerwacja)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;