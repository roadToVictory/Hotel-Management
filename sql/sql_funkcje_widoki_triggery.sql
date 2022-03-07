CREATE OR REPLACE VIEW hotel.rezerwacje(id_rezerwacja, imie, nazwisko,  kod, miasto, ulica, numer_domu, numer_pokoju, opis_kategorii, poczatek, koniec, wyzywienie_kategoria, codzienne_sprzatanie) AS
select rez.id_rezerwacja, os.imie, os.nazwisko, ad.kod_pocztowy, ad.miejscowosc, ad.ulica, ad.numer, pok.numer, kat.opis_kategorii, t.termin_poczatkowy, t.termin_koncowy, wyz.kategoria, sp.czy_codziennie
FROM hotel.osoba os 
JOIN hotel.adres ad using(id_osoba)
JOIN hotel.rezerwacja rez using(id_osoba)
JOIN hotel.kategoria kat using(id_kategoria)
JOIN hotel.termin t using(id_termin)
JOIN hotel.pokoj pok using(id_kategoria)
JOIN hotel.wyzywienie wyz using(id_osoba)
JOIN hotel.sprzatanie sp using(id_rezerwacja)
JOIN hotel.termin_pokoj tp using(id_termin)
where tp.numer=pok.numer and t.id_termin = tp.id_termin
GROUP BY rez.id_rezerwacja, os.imie, os.nazwisko, ad.kod_pocztowy, ad.miejscowosc, ad.ulica, ad.numer, pok.numer, kat.opis_kategorii, t.termin_poczatkowy, t.termin_koncowy, wyz.kategoria, sp.czy_codziennie
ORDER BY 1;


CREATE OR REPLACE VIEW hotel.pokoj_info(id_osoba, id_rezerwacja, imie, nazwisko, numer_dowodu, czas_pobytu, kwota_za_dzien, dodatkowe_koszty) AS
SELECT os.id_osoba, rez.id_rezerwacja, os.imie, os.nazwisko, os.numer_dowodu, DATE_PART('day', t.termin_koncowy::TIMESTAMP-t.termin_poczatkowy::TIMESTAMP), kat.cena, COALESCE(SUM(dod.oplata), 0)
from hotel.osoba os
JOIN hotel.rezerwacja rez using(id_osoba)
FULL OUTER JOIN hotel.dodatkowa_oplata dod using(id_rezerwacja)
JOIN hotel.termin t using(id_termin)
JOIN hotel.kategoria kat using(id_kategoria)
group by rez.id_rezerwacja, os.id_osoba, t.id_termin, kat.id_kategoria
order by id_rezerwacja;

CREATE OR REPLACE VIEW hotel.pokoj_rachunek(id_osoba, imie, nazwisko, numer_dowodu, suma) AS WITH cte AS
(SELECT p.id_osoba, SUM(p.czas_pobytu*p.kwota_za_dzien+p.dodatkowe_koszty) AS suma from hotel.pokoj_info p GROUP by p.id_rezerwacja, p.id_osoba ORDER by p.id_rezerwacja)
SELECT id_osoba, imie, nazwisko,numer_dowodu, suma from hotel.osoba JOIN cte USING(id_osoba);

CREATE OR REPLACE VIEW hotel.wyzywienie_rachunek(id_osoba, imie, nazwisko, suma) AS SELECT os.id_osoba, os.imie, os.nazwisko, COALESCE(SUM(wyz.cena*p.czas_pobytu),0) FROM hotel.osoba os JOIN hotel.pokoj_info p using(id_osoba) 
FULL OUTER JOIN hotel.wyzywienie wyz using(id_osoba)
GROUP by os.id_osoba, p.id_rezerwacja;


CREATE OR REPLACE VIEW hotel.sprzatanie_rachunek(id_osoba, imie, nazwisko, suma) AS WITH cte AS (SELECT os.id_osoba, 
CASE sp.czy_codziennie
	WHEN false THEN SUM(sp.cena/2)
	WHEN true THEN SUM(DATE_PART('day', t.termin_koncowy::TIMESTAMP-t.termin_poczatkowy::TIMESTAMP)*sp.cena)
END as suma
from hotel.sprzatanie sp 
JOIN hotel.rezerwacja using(id_rezerwacja) 
JOIN hotel.termin t using(id_termin)
FULL OUTER JOIN hotel.osoba os using(id_osoba)
GROUP by sp.czy_codziennie, os.id_osoba)
SELECT os.id_osoba, os.imie, os.nazwisko, sum(suma) as suma FROM cte JOIN hotel.osoba os using(id_osoba) JOIN hotel.rezerwacja using(id_osoba)
GROUP by os.id_osoba, rezerwacja.id_rezerwacja
ORDER BY os.id_osoba;


------------------------------------------------------------
CREATE OR REPLACE VIEW hotel.podsumowanie_rachunku(id_osoba, imie, nazwisko, oplata_za_pokoj, oplata_za_wyzywienie, oplata_za_sprzatanie, dodatkowe_oplaty,   suma) AS
SELECT os.id_osoba, os.imie, os.nazwisko, pok.suma as oplata_za_pokoj, (COALESCE(wyz.suma,0)) as oplata_za_wyzywienie, (COALESCE(sp.suma,0)) as oplata_za_sprzatanie, (COALESCE(pin.dodatkowe_koszty, 0)) as dodatkowe_oplaty, (pok.suma+COALESCE(wyz.suma,0)+COALESCE(sp.suma,0)+COALESCE(pin.dodatkowe_koszty, 0)) as suma from hotel.osoba os 
JOIN hotel.pokoj_rachunek pok using(id_osoba)
FULL JOIN  hotel.wyzywienie_rachunek wyz using(id_osoba)
LEFT JOIN  hotel.sprzatanie_rachunek sp using(id_osoba)
LEFT JOIN hotel.pokoj_info pin using(id_osoba)
group by os.id_osoba, pok.suma, wyz.suma, sp.suma,pin.dodatkowe_koszty
order by os.id_osoba;

-------------------------------------------------------------

CREATE OR REPLACE VIEW hotel.kupon(id_osoba, imie, nazwisko, ile_pobytow) AS WITH cte AS
(SELECT numer_dowodu, id_osoba, COUNT(id_rezerwacja) as ile_pobytow from hotel.osoba JOIN hotel.rezerwacja using(id_osoba) group by numer_dowodu, id_osoba HAVING COUNT(id_rezerwacja)>=2)
SELECT id_osoba, imie, nazwisko, ile_pobytow from hotel.osoba join cte using(id_osoba);

CREATE OR REPLACE VIEW hotel.goscie(id_gosc, imie, nazwisko, rezerwujacy, nazwisko_rez) AS
SELECT lg.id_gosc, lg.imie, lg.nazwisko, os.imie, os.nazwisko from hotel.lista_gosci lg JOIN hotel.osoba os using(id_osoba);

CREATE OR REPLACE VIEW hotel.ranking(id_osoba, imie, nazwisko, koszt) AS WITH cte AS (SELECT pr.id_osoba, SUM(pr.suma) AS suma from hotel.podsumowanie_rachunku pr GROUP by id_osoba HAVING SUM(pr.suma)>10000 order by suma ASC, pr.id_osoba )
SELECT id_osoba, imie, nazwisko, suma FROM hotel.osoba JOIN cte using(id_osoba);


CREATE OR REPLACE VIEW hotel.wolne_pokoje(id_kategoria, numer, opis_kategorii, cena) AS WITH cte AS (select numer from hotel.pokoj except select numer from hotel.termin_pokoj join hotel.termin using(id_termin) where termin_koncowy::TIMESTAMP>now())
SELECT kat.id_kategoria, cte.numer, kat.opis_kategorii, kat.cena FROM hotel.kategoria kat JOIN hotel.pokoj using(id_kategoria) JOIN cte using(numer);

-------------------------------------------------------------------------------- 

CREATE OR REPLACE FUNCTION hotel.pokoj_valid() RETURNS TRIGGER AS
$$
DECLARE pok RECORD;
i INTEGER:=0;
BEGIN
	FOR pok IN(SELECT numer from hotel.pokoj where id_kategoria=NEW.id_kategoria)
	LOOP
		IF NOT EXISTS (SELECT * from dostepnosc WHERE numer=pok.numer) THEN
			INSERT INTO hotel.termin_pokoj(id_termin, numer) VALUES (NEW.id_termin, pok.numer);
			RETURN NULL; --
		END IF;
	END LOOP;
	RETURN NULL;
END;

$$ LANGUAGE 'plpgsql';

CREATE TRIGGER walidacja_termin_pokoj AFTER INSERT OR UPDATE ON hotel.rezerwacja FOR EACH ROW EXECUTE PROCEDURE hotel.pokoj_valid();

-------------------------------------------------------------------------------- 

CREATE OR REPLACE FUNCTION hotel.rezerwacja_valid() RETURNS TRIGGER AS 
$$
DECLARE
	koniec hotel.termin.termin_koncowy%TYPE;
	poczatek hotel.termin.termin_poczatkowy%TYPE;
	n_koniec hotel.termin.termin_koncowy%TYPE;
	n_poczatek hotel.termin.termin_poczatkowy%TYPE;
	ile hotel.kategoria.liczba_pokoi%TYPE;
	rez RECORD;
	pok RECORD;
	i INTEGER:=0;
BEGIN
	SELECT into poczatek termin_poczatkowy from hotel.termin where id_termin=NEW.id_termin;
	SELECT into koniec termin_koncowy from hotel.termin where id_termin=NEW.id_termin;
	CREATE TEMPORARY TABLE IF NOT EXISTS dostepnosc(numer INTEGER);
	
	for rez IN (SELECT id_termin, id_kategoria from hotel.rezerwacja)
	LOOP
		SELECT into n_poczatek termin_poczatkowy from hotel.termin where id_termin=rez.id_termin;
		SELECT into n_koniec termin_koncowy from hotel.termin where id_termin=rez.id_termin;
		IF (NOT(n_poczatek > koniec OR poczatek > n_koniec)) and NEW.id_kategoria=rez.id_kategoria THEN
			INSERT INTO dostepnosc SELECT numer FROM hotel.termin_pokoj WHERE id_termin=rez.id_termin;
			i:=i+1;
		END IF;
	END LOOP;
	
	SELECT INTO ile liczba_pokoi from hotel.kategoria WHERE id_kategoria = NEW.id_kategoria;
	IF i<ile THEN
		FOR pok IN(SELECT numer FROM hotel.pokoj WHERE id_kategoria=NEW.id_kategoria)
		LOOP
			IF NOT EXISTS (SELECT * from dostepnosc WHERE numer=pok.numer) THEN
				RETURN NEW;  --rezerwacja
			END IF;
		END LOOP;
	END IF;
	RAISE EXCEPTION 'Brak dostepnych pokoi w wybranej kategorii i terminie!';
	RETURN NULL;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER walidacja_rezerwacji BEFORE INSERT OR UPDATE ON hotel.rezerwacja FOR EACH ROW EXECUTE PROCEDURE hotel.rezerwacja_valid();

-------------------------------------------------------------------------------- 


CREATE OR REPLACE FUNCTION hotel.dodaj_zmien_pokoj() RETURNS TRIGGER AS
$$
BEGIN
	IF (TG_OP = 'DELETE') THEN
        UPDATE hotel.kategoria SET liczba_pokoi = liczba_pokoi-1 where id_kategoria=OLD.id_kategoria;
        RETURN NULL;
    ELSIF (TG_OP = 'UPDATE') THEN
        UPDATE hotel.kategoria SET liczba_pokoi = liczba_pokoi-1 where id_kategoria=OLD.id_kategoria; --zmiana kategorii pokoju
        UPDATE hotel.kategoria SET liczba_pokoi = liczba_pokoi+1 where id_kategoria=NEW.id_kategoria;
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        UPDATE hotel.kategoria SET liczba_pokoi = liczba_pokoi+1 where id_kategoria=NEW.id_kategoria;  --dodanie pokoju do kategorii
		RETURN NEW;
    END IF;
    RETURN NULL; --
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER pokoj_mod AFTER INSERT OR UPDATE OR DELETE ON hotel.pokoj FOR EACH ROW EXECUTE PROCEDURE hotel.dodaj_zmien_pokoj();

-------------------------------------------------------------------------------- 

CREATE OR REPLACE FUNCTION hotel.termin_valid() RETURNS TRIGGER AS
$$
BEGIN
	IF NEW.termin_koncowy > NEW.termin_poczatkowy and CURRENT_DATE < NEW.termin_poczatkowy and NEW.termin_koncowy - CURRENT_DATE < 90 THEN
		RETURN NEW;
	ELSE
		RAISE NOTICE 'Niepoprawna data! Format daty: YYYY-MM-DD!		Rezerwacji mozna dokonac maksymalnie do 3 miesiecy wprzod!';
		RETURN NULL;
	END IF;
	RETURN NULL;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER walidacja_terminu BEFORE INSERT OR UPDATE ON hotel.termin FOR EACH ROW EXECUTE PROCEDURE hotel.termin_valid();

-------------------------------------------------------------------------------- 


CREATE OR REPLACE FUNCTION hotel.osoba_dane_valid() RETURNS TRIGGER AS
$$ 
BEGIN
	IF length(NEW.imie) < 2 THEN 
		RAISE EXCEPTION 'Za krotkie imie!';
	ELSIF length(NEW.nazwisko) < 2 THEN 
		RAISE EXCEPTION 'Za krotkie nazwisko!';
	ELSIF NEW.numer_dowodu !~ '^[a-zA-Z]{3}\d{6}$' THEN
		RAISE EXCEPTION 'Niepoprawny numer dowodu! Jego poprawny format to: LLLDDDDDD';
	ELSIF NEW.email NOT LIKE '%_@__%.__%'  THEN --najprostszy (chyba) sposob walidacji email w postgresql
		RAISE EXCEPTION 'Niepoprawny adres email!';
	END IF;
RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER walidacja_danych BEFORE INSERT ON hotel.osoba FOR EACH ROW EXECUTE PROCEDURE hotel.osoba_dane_valid();

-------------------------------------------------------------------------------- 

CREATE OR REPLACE FUNCTION hotel.edytuj_adres_osoby(VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR,VARCHAR) RETURNS text AS
$$
DECLARE
id hotel.adres.id_osoba%TYPE;
flaga boolean;
BEGIN
	SELECT into id id_osoba from hotel.osoba where imie=$1 and nazwisko=$2; 
	SELECT EXISTS(SELECT * FROM hotel.adres WHERE id_osoba = id) INTO flaga;
	IF flaga and length($1)>2 and length($2)>2 and length($3)=6 and $3 LIKE '[0-9]{2}-[0-9]{3}' and length($4)>2 and length($5)>2 and LENGTH($6)>0 THEN
		UPDATE hotel.adres SET kod_pocztowy = $3, miejscowosc = $4, ulica=$5, numer=$6 WHERE id_osoba = id;
		RETURN 'Zmienono adres pomyslnie!';
	ELSE
		RETURN 'Wystapil blad! Upewnij sie ze podane dane sa prawidlowe';
	END IF;
END; 
$$ LANGUAGE 'plpgsql';

-------------------------------------------------------------------------------- 


CREATE OR REPLACE FUNCTION hotel.dodaj_pracownika(name VARCHAR, surname VARCHAR, login VARCHAR, password VARCHAR) RETURNS boolean AS
$$
BEGIN
    INSERT INTO hotel.pracownicy(imie, nazwisko, login, haslo) VALUES ($1, $2, $3, $4);

	IF FOUND THEN
		RETURN TRUE;
	ELSE 
RETURN FALSE;
END IF;
END
$$
  LANGUAGE 'plpgsql';


-------------------------------------------------------------------------------- 

CREATE OR REPLACE FUNCTION hotel.logowanie(log VARCHAR, pass VARCHAR) RETURNS table(id INTEGER, loggin varchar, password varchar) AS
$$
BEGIN
    RETURN QUERY(select pracownicy.id_pracownik, pracownicy.login, pracownicy.haslo from hotel.pracownicy where login=$1 and haslo=$2);
END
$$
  LANGUAGE 'plpgsql';

-------------------------------------------------------------------------------- 

create or replace function hotel.normalize_name() returns trigger language 'plpgsql' as $$ declare
first_letter text; anoth text;
begin
first_letter:=upper(substring(new.imie for 1));
anoth:=lower(substring(new.imie from 2));
new.imie:=first_letter||anoth;
first_letter:=upper(substring(new.nazwisko for 1));
anoth:=lower(substring(new.nazwisko from 2));
new.nazwisko:=first_letter||anoth;
return new;
end; $$;

create trigger osoba_norm before insert or update on hotel.osoba for each row execute procedure hotel.normalize_name();
create trigger gosc_norm before insert or update on hotel.lista_gosci for each row execute procedure hotel.normalize_name();