package cz.muni.fi.dictatetrainer.corrector.rules.impl;

import java.util.*;
//TODO does it initialize only once?

/**
 * List holds the static lists as defined in pseudocode beside correcting rules, made by Vojtěch Škvařil
 */
public class CorrectorRulesStaticLists {

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_B_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = -7908867944691675527L;

        {
            put("být", "být (ve škole) x bít (nepřítele)");
            put("bít", "být (ve škole) x bít (nepřítele)");
            put("dobýt", "dobýt (hrad, úspěch) x dobít (baterii, telefon, kredit)");
            put("dobít", "dobýt (hrad, úspěch) x dobít (baterii, telefon, kredit)");
            put("odbýt", "odbýt (práci, nápadníka) x odbít (poledne)");
            put("odbít", "odbýt (práci, nápadníka) x odbít (poledne)");
            put("přibýt", "přibýt (na váze) x přibít (hřebík)");
            put("přibít", "přibýt (na váze) x přibít (hřebík)");
            put("pobýt", "pobýt (nějaký čas někde) x pobít (střechu plechem, nepřátele)");
            put("pobít", "pobýt (nějaký čas někde) x pobít (střechu plechem, nepřátele)");
            put("vybýt", "vybýt (= zbýt) x vybít (energii, zvěř)");
            put("vybít", "vybýt (= zbýt) x vybít (energii, zvěř)");
            put("nabýt", "nabýt (majetek, vědomosti) x nabít (zbraň, dítěti)");
            put("nabít", "nabýt (majetek, vědomosti) x nabít (zbraň, dítěti)");
            put("ubýt", "ubýt (na váze) x ubít (draka, čas)");
            put("ubít", "ubýt (na váze) x ubít (draka, čas)");
            put("zbít", "zbýt (= zůstat) x zbít (psa)");
            put("zbýt", "zbýt (= zůstat) x zbít (psa)");
            put("zabít", "zabít (komára) x (tvar zabýt neexistuje)");
            put("rozbít", "rozbít(vázu) x (tvar rozbýt neexistuje)");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_L_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = -311574924598768700L;

        {
            put("vlys", "vlis (k vlisovat) x vlys (ozdobný pruh na stěně)");
            put("vlis", "vlis (k vlisovat) x vlys (ozdobný pruh na stěně)");
            put("lýčený", "lýčený (z lýka) x líčený (předstíraný)");
            put("líčený", "lýčený (z lýka) x líčený (předstíraný)");
            put("lyže", "lyže x ližina (trámec, podklad pod břemeno)");
            put("ližina", "lyže x ližina (trámec, podklad pod břemeno)");
            put("lysý", "lysý (plešatý) x lísat se, lišaj, lišej");
            put("lísat", "lysý (plešatý) x lísat se, lišaj, lišej");
            put("lišaj", "lysý (plešatý) x lísat se, lišaj, lišej");
            put("lišej", "lysý (plešatý) x lísat se, lišaj, lišej");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_M_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = -8404421261215070773L;

        {
            put("mlynář", "mlít (maso) x mlynář (povolání)");
            put("mlít", "mlít (maso) x mlynář (povolání)");
            put("my", "my (zájmeno 1. os. mn. č. (např. my umíráme)) x mi (3. pád zájmena já, např. pošli mi lékárničku)");
            put("mi", "my (zájmeno 1. os. mn. č. (např. my umíráme)) x mi (3. pád zájmena já, např. pošli mi lékárničku)");

        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_P_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = 8883907861616111098L;

        {
            put("pýchavka", "pýchavka (houba) x píchat (bodat)");
            put("píchat", "pýchavka (houba) x píchat (bodat)");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_S_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = -1399382060882178435L;

        {
            put("sýrový", "sýrový, sýr x síra, sírový (chemický prvek, např. oxid sírový)");
            put("sýr", "sýrový, sýr x síra, sírový (chemický prvek, např. oxid sírový)");
            put("síra", "sýrový, sýr x síra, sírový (chemický prvek, např. oxid sírový)");
            put("sírový", "sýrový, sýr x síra, sírový (chemický prvek, např. oxid sírový)");
        }
    };
    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_V_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = 218851161278264938L;

        {
            put("výr", "výr (druh sovy, např. výr velký) x vír (krouživý pohyb vody nebo vzduchu, např. vodní vír)");
            put("vír", "výr (druh sovy, např. výr velký) x vír (krouživý pohyb vody nebo vzduchu, např. vodní vír)");
            put("vysutý", "vysutý (vysunutý, předpona vy-, např. vysutý šuplík) x visutý (visící, např. visutý most)");
            put("visutý", "vysutý (vysunutý, předpona vy-, např. vysutý šuplík) x visutý (visící, např. visutý most)");

        }
    };
    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_Z_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = 119778604227514323L;

        {
            put("nazývat", "nazývat se (jmenovat se) x nazívat se (mnoho zívat)");
            put("nazívat", "nazývat se (jmenovat se) x nazívat se (mnoho zívat)");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_B = new HashMap<String, String>() {
        private static final long serialVersionUID = 5076158331930372261L;
        private final String DEF_BYT = "být (bych, bys, by, bychom, byste, abych, abys, aby, …, kdybych, kdybys, " +
                "kdyby, …, bytí, živobytí, bývat, bývalý, byt, bytná, bytový, bytelný, bytost, bydlit, bydliště, " +
                "obydlí, bydlo (příbytek, živobytí), dobýt, dobyvatel, dobytek, dobytče, dobytkářství, nabýt, nabývat, " +
                "nábytek, obývat, obyvatel, obyvatelstvo, odbýt, odbyt, neodbytný, pozbýt, přebýt, přebývat, přebytek, " +
                "přibýt, přibývat, příbytek, ubýt, ubývat, úbytek, zbývat, zbytek, zabývat se)";

        {
            put("být", DEF_BYT);
            put("bych", DEF_BYT);
            put("bys", DEF_BYT);
            put("by", DEF_BYT);
            put("bychom", DEF_BYT);
            put("byste", DEF_BYT);
            put("abych", DEF_BYT);
            put("abys", DEF_BYT);
            put("aby", DEF_BYT);
            put("kdybych", DEF_BYT);
            put("kdybys", DEF_BYT);
            put("kdyby", DEF_BYT);
            put("bytí", DEF_BYT);
            put("živobytí", DEF_BYT);
            put("bývat", DEF_BYT);
            put("bývalý", DEF_BYT);
            put("byt", DEF_BYT);
            put("bytná", DEF_BYT);
            put("bytový", DEF_BYT);
            put("bytelný", DEF_BYT);
            put("bytost", DEF_BYT);
            put("bydlit", DEF_BYT);
            put("bydliště", DEF_BYT);
            put("obydlí", DEF_BYT);
            put("bydlo", DEF_BYT);
            put("příbytek", DEF_BYT);
            put("živobytí", DEF_BYT);
            put("dobýt", DEF_BYT);
            put("dobyvatel", DEF_BYT);
            put("dobytek", DEF_BYT);
            put("dobytče", DEF_BYT);
            put("dobytkářství", DEF_BYT);
            put("nabýt", DEF_BYT);
            put("nabývat", DEF_BYT);
            put("nábytek", DEF_BYT);
            put("obývat", DEF_BYT);
            put("obyvatel", DEF_BYT);
            put("obyvatelstvo", DEF_BYT);
            put("odbýt", DEF_BYT);
            put("odbyt", DEF_BYT);
            put("neodbytný", DEF_BYT);
            put("pozbýt", DEF_BYT);
            put("přebýt", DEF_BYT);
            put("přebývat", DEF_BYT);
            put("přebytek", DEF_BYT);
            put("přibýt", DEF_BYT);
            put("přibývat", DEF_BYT);
            put("příbytek", DEF_BYT);
            put("ubýt", DEF_BYT);
            put("ubývat", DEF_BYT);
            put("úbytek", DEF_BYT);
            put("zbývat", DEF_BYT);
            put("zbytek", DEF_BYT);
            put("zabývat", DEF_BYT);
            put("obyčej", "obyčej (obyčejný)");
            put("obyčejný", "obyčej (obyčejný)");
            put("bystrý", "bystrý (bystře, bystrost, bystřina, Bystřice)");
            put("bystře", "bystrý (bystře, bystrost, bystřina, Bystřice)");
            put("bystrost", "bystrý (bystře, bystrost, bystřina, Bystřice)");
            put("bystřina", "bystrý (bystře, bystrost, bystřina, Bystřice)");
            put("Bystřice", "bystrý (bystře, bystrost, bystřina, Bystřice)");
            put("bylina", "bylina (býlí, býložravec, černobýl, zlatobýl)");
            put("býlí", "bylina (býlí, býložravec, černobýl, zlatobýl)");
            put("býložravec", "bylina (býlí, býložravec, černobýl, zlatobýl)");
            put("černobýl", "bylina (býlí, býložravec, černobýl, zlatobýl)");
            put("zlatobýl", "bylina (býlí, býložravec, černobýl, zlatobýl)");
            put("kobyla", "kobyla (kobylka, Kobylisy)");
            put("kobylka", "kobyla (kobylka, Kobylisy)");
            put("Kobylisy", "kobyla (kobylka, Kobylisy)");
            put("býk", "býk (býček, býčí, býkovec)");
            put("býček", "býk (býček, býčí, býkovec)");
            put("býčí", "býk (býček, býčí, býkovec)");
            put("býkovec", "býk (býček, býčí, býkovec)");
            put("babyka", "babyka");
            put("Bydžov", "Bydžov");
            put("Přibyslav", "Přibyslav");
            put("Bylany", "Bylany");
            put("Hrabyně", "Hrabyně");
            put("Zbyněk", "Zbyněk");

        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_L = new HashMap<String, String>() {
        private static final long serialVersionUID = -1845419840137956343L;
        private final String DEF_PLYN = "plynout (uplynout, rozplynout se, rozplývat se, splynout, splývat, oplývat, " +
                "vyplývat, plynulý, plyn, plynný, plynárna, plynoměr, plynojem)";

        {
            put("slyšet", "slyšet (slyšitelný, slýchat, nedoslýchavý)");
            put("slyšitelný", "slyšet (slyšitelný, slýchat, nedoslýchavý)");
            put("slýchat", "slyšet (slyšitelný, slýchat, nedoslýchavý)");
            put("nedoslýchavý", "slyšet (slyšitelný, slýchat, nedoslýchavý)");
            put("mlýn", "mlýn (mlynář, mlýnice)");
            put("mlynář", "mlýn (mlynář, mlýnice)");
            put("mlýnice", "mlýn (mlynář, mlýnice)");
            put("blýskat se", "blýskat se (blýsknout se, zablýsknout se, blýskavice, blýskavý, blyštět se)");
            put("blýsknout se", "blýskat se (blýsknout se, zablýsknout se, blýskavice, blýskavý, blyštět se)");
            put("zablýsknout se", "blýskat se (blýsknout se, zablýsknout se, blýskavice, blýskavý, blyštět se)");
            put("blýskavice", "blýskat se (blýsknout se, zablýsknout se, blýskavice, blýskavý, blyštět se)");
            put("blýskavý", "blýskat se (blýsknout se, zablýsknout se, blýskavice, blýskavý, blyštět se)");
            put("blyštět se", "blýskat se (blýsknout se, zablýsknout se, blýskavice, blýskavý, blyštět se)");
            put("polykat", "polykat (zalykat se)");
            put("zalykat se", "polykat (zalykat se)");
            put("vzlykat", "vzlykat (vzlyknout, vzlyk, vzlykot)");
            put("vzlyknout", "vzlykat (vzlyknout, vzlyk, vzlykot)");
            put("vzlyk", "vzlykat (vzlyknout, vzlyk, vzlykot)");
            put("vzlykot", "vzlykat (vzlyknout, vzlyk, vzlykot)");
            put("plynout", DEF_PLYN);
            put("uplynout", DEF_PLYN);
            put("rozplynout se", DEF_PLYN);
            put("rozplývat se", DEF_PLYN);
            put("splynout", DEF_PLYN);
            put("splývat", DEF_PLYN);
            put("oplývat", DEF_PLYN);
            put("vyplývat", DEF_PLYN);
            put("plynulý", DEF_PLYN);
            put("plyn", DEF_PLYN);
            put("plynárna", DEF_PLYN);
            put("plynoměr", DEF_PLYN);
            put("plynojem", DEF_PLYN);
            put("plynný", DEF_PLYN);
            put("plýtvat", "plýtvat");
            put("lysý", "lysý (lysina, lyska, Lysá, Lysolaje)");
            put("lysina", "lysý (lysina, lyska, Lysá, Lysolaje)");
            put("lyska", "lysý (lysina, lyska, Lysá, Lysolaje)");
            put("Lysá", "lysý (lysina, lyska, Lysá, Lysolaje)");
            put("Lysolaje", "lysý (lysina, lyska, Lysá, Lysolaje)");
            put("lýtko", "lýtko");
            put("lýko", "lýko (lýčí, lýčený (tj. lýkový), lýkovec, lýkožrout)");
            put("lýčí", "lýko (lýčí, lýčený (tj. lýkový), lýkovec, lýkožrout)");
            put("lýčený", "lýko (lýčí, lýčený (tj. lýkový), lýkovec, lýkožrout)");
            put("lýkový", "lýko (lýčí, lýčený (tj. lýkový), lýkovec, lýkožrout)");
            put("lýkovec", "lýko (lýčí, lýčený (tj. lýkový), lýkovec, lýkožrout)");
            put("lýkožrout", "lýko (lýčí, lýčený (tj. lýkový), lýkovec, lýkožrout)");
            put("lyže", "lyže (lyžovat, lyžař)");
            put("lyžovat", "lyže (lyžovat, lyžař)");
            put("lyžař", "lyže (lyžovat, lyžař)");
            put("pelyněk", "pelyněk");
            put("plyš", "plyš");
            put("slynout", "slynout");
            put("plytký", "plytký");
            put("vlys", "vlys (vlýsek, vlysový)");
            put("vlýsek", "vlys (vlýsek, vlysový)");
            put("vlysový", "vlys (vlýsek, vlysový)");
            put("Volyně", "Volyně");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_M = new HashMap<String, String>() {
        private static final long serialVersionUID = 2388492508460475025L;
        private final String DEF_MYT = "mýt (mycí, myčka, umýt, umývat, umývadlo i umyvadlo, umývárna i umyvárna, pomyje, " +
                "mýval, mýdlo, mydlit, mydlař, mydlina)";
        private final String DEF_MYSL = "myslit i myslet (mysl, myšlenka, pomyslit i pomyslet, pomýšlet, přemýšlet, " +
                "vymyslit i vymyslet, vymýšlet, výmysl, úmysl, usmyslit si i usmyslet si, smýšlení, smyšlenka, " +
                "smysl, smyslný, nesmyslný, průmysl, myslivec, myslivna, Nezamysl, Nezamyslice, Přemysl)";

        {
            put("mýt", DEF_MYT);
            put("mycí", DEF_MYT);
            put("myčka", DEF_MYT);
            put("umýt", DEF_MYT);
            put("umývat", DEF_MYT);
            put("umývadlo", DEF_MYT);
            put("umyvadlo", DEF_MYT);
            put("umývárna", DEF_MYT);
            put("umyvárna", DEF_MYT);
            put("pomyje", DEF_MYT);
            put("mýval", DEF_MYT);
            put("mýdlo", DEF_MYT);
            put("mydlit", DEF_MYT);
            put("mydlař", DEF_MYT);
            put("mydlina", DEF_MYT);
            put("myslit", DEF_MYSL);
            put("myslet", DEF_MYSL);
            put("mysl", DEF_MYSL);
            put("myšlenka", DEF_MYSL);
            put("pomyslit", DEF_MYSL);
            put("pomyslet", DEF_MYSL);
            put("pomýšlet", DEF_MYSL);
            put("přemýšlet", DEF_MYSL);
            put("vymyslit", DEF_MYSL);
            put("vymyslet", DEF_MYSL);
            put("vymýšlet", DEF_MYSL);
            put("výmysl", DEF_MYSL);
            put("úmysl", DEF_MYSL);
            put("usmyslit si", DEF_MYSL);
            put("usmyslet si", DEF_MYSL);
            put("smýšlení", DEF_MYSL);
            put("smyšlenka", DEF_MYSL);
            put("smysl", DEF_MYSL);
            put("smyslný", DEF_MYSL);
            put("nesmyslný", DEF_MYSL);
            put("průmysl", DEF_MYSL);
            put("myslivec", DEF_MYSL);
            put("myslivna", DEF_MYSL);
            put("Nezamysl", DEF_MYSL);
            put("Nezamyslice", DEF_MYSL);
            put("Přemysl", DEF_MYSL);
            put("mýlit se", "mýlit se (mýlka, mylný, omyl, zmýlená)");
            put("mýlka", "mýlit se (mýlka, mylný, omyl, zmýlená)");
            put("mylný", "mýlit se (mýlka, mylný, omyl, zmýlená)");
            put("omyl", "mýlit se (mýlka, mylný, omyl, zmýlená)");
            put("zmýlená", "mýlit se (mýlka, mylný, omyl, zmýlená)");
            put("hmyz", "hmyz (hmyzí, hmyzožravec)");
            put("hmyzí", "hmyz (hmyzí, hmyzožravec)");
            put("hmyzožravec", "hmyz (hmyzí, hmyzožravec)");
            put("myš", "myš (myší, myšina)");
            put("myší", "myš (myší, myšina)");
            put("myšina", "myš (myší, myšina)");
            put("mýtit", "mýtit (mýtina, vymýtit, vymycovat)");
            put("mýtina", "mýtit (mýtina, vymýtit, vymycovat)");
            put("vymýtit", "mýtit (mýtina, vymýtit, vymycovat)");
            put("vymycovat", "mýtit (mýtina, vymýtit, vymycovat)");
            put("zamykat", "zamykat (odmykat, nedomykat, vymykat se, výmyk, vymycovat)");
            put("odmykat", "zamykat (odmykat, nedomykat, vymykat se, výmyk, vymycovat)");
            put("nedomykat", "zamykat (odmykat, nedomykat, vymykat se, výmyk, vymycovat)");
            put("vymykat se", "zamykat (odmykat, nedomykat, vymykat se, výmyk, vymycovat)");
            put("výmyk", "zamykat (odmykat, nedomykat, vymykat se, výmyk, vymycovat)");
            put("vymycovat", "zamykat (odmykat, nedomykat, vymykat se, výmyk, vymycovat)");
            put("smýkat", "smýkat (smyk, smýčit, smyčec, smyčka, průsmyk)");
            put("smyk", "smýkat (smyk, smýčit, smyčec, smyčka, průsmyk)");
            put("smýčit", "smýkat (smyk, smýčit, smyčec, smyčka, průsmyk)");
            put("smyčec", "smýkat (smyk, smýčit, smyčec, smyčka, průsmyk)");
            put("smyčka", "smýkat (smyk, smýčit, smyčec, smyčka, průsmyk)");
            put("průsmyk", "smýkat (smyk, smýčit, smyčec, smyčka, průsmyk)");
            put("dmýchat", "dmýchat (rozdmýchat, dmychadlo i dmýchadlo)");
            put("rozdmýchat", "dmýchat (rozdmýchat, dmychadlo i dmýchadlo)");
            put("dmychadlo", "dmýchat (rozdmýchat, dmychadlo i dmýchadlo)");
            put("dmýchadlo", "dmýchat (rozdmýchat, dmychadlo i dmýchadlo)");
            put("nachomýtnout se", "nachomýtnout se (ochomýtat se)");
            put("ochomýtat se", "nachomýtnout se (ochomýtat se)");
            put("mýto", "mýto (mýtné, Mýto)");
            put("mýtné", "mýto (mýtné, Mýto)");
            put("Mýto", "mýto (mýtné, Mýto)");
            put("mykat", "mykat (mykaný)");
            put("mykaný", "mykat (mykaný)");
            put("mys", "mys");
            put("sumýš", "sumýš");
            put("Litomyšl", "Litomyšl");
            put("Kamýk", "Kamýk");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_P = new HashMap<String, String>() {
        private static final long serialVersionUID = 8631559833095829705L;

        {
            put("pýcha", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("pyšný", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("pyšnit se", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("zpychnout", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("pýchavka", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("pych", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("přepych", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("Přepychy", "pýcha (pyšný, pyšnit se, zpychnout, pýchavka, pych, přepych, Přepychy)");
            put("pytel", "pytel (pytlovina, pytlák, pytlačit)");
            put("pytlovina", "pytel (pytlovina, pytlák, pytlačit)");
            put("pytlák", "pytel (pytlovina, pytlák, pytlačit)");
            put("pytlačit", "pytel (pytlovina, pytlák, pytlačit)");
            put("pysk", "pysk (pyskatý, ptakopysk, Solopysky)");
            put("pyskatý", "pysk (pyskatý, ptakopysk, Solopysky)");
            put("ptakopysk", "pysk (pyskatý, ptakopysk, Solopysky)");
            put("Solopysky", "pysk (pyskatý, ptakopysk, Solopysky)");
            put("pyl", "pyl (opylovat)");
            put("opylovat", "pyl (opylovat)");
            put("kopyto", "kopyto (sudokopytník)");
            put("sudokopytník", "kopyto (sudokopytník)");
            put("klopýtat", "klopýtat (klopýtnout)");
            put("klopýtnout", "klopýtat (klopýtnout)");
            put("třpytit se", "třpytit se (třpyt, třpytivý, třpytka)");
            put("třpyt", "třpytit se (třpyt, třpytivý, třpytka)");
            put("třpytivý", "třpytit se (třpyt, třpytivý, třpytka)");
            put("třpytka", "třpytit se (třpyt, třpytivý, třpytka)");
            put("zpytovat", "zpytovat (jazykozpyt, nevyzpytatelný)");
            put("jazykozpyt", "zpytovat (jazykozpyt, nevyzpytatelný)");
            put("nevyzpytatelný", "zpytovat (jazykozpyt, nevyzpytatelný)");
            put("pykat", "pykat (odpykat)");
            put("odpykat", "pykat (odpykat)");
            put("pýr", "pýr (pýřavka)");
            put("pýřavka", "pýr (pýřavka)");
            put("pýří", "pýří");
            put("pýřit se", "pýřit se (zapýřit se, pýřivý, čepýřit se)");
            put("zapýřit se", "pýřit se (zapýřit se, pýřivý, čepýřit se)");
            put("pýřivý", "pýřit se (zapýřit se, pýřivý, čepýřit se)");
            put("čepýřit se", "pýřit se (zapýřit se, pýřivý, čepýřit se)");
            put("pyj", "pyj");
            put("Chropyně", "Chropyně");
            put("Pyšely", "Pyšely");
            put("Spytihněv", "Spytihněv");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_S = new HashMap<String, String>() {
        private static final long serialVersionUID = 6126349093196150803L;
        private final String DEF_SYP = "sypat (sypký, sýpka, sypek, nasypat, násyp, násypný, osypaný, " +
                "vysypat, zasypat, zásyp)";

        {
            put("syn", "syn (synovský, synovec, zlosyn)");
            put("synovský", "syn (synovský, synovec, zlosyn)");
            put("synovec", "syn (synovský, synovec, zlosyn)");
            put("zlosyn", "syn (synovský, synovec, zlosyn)");
            put("sytý", "sytý (sytost, dosyta, nasytit, nenasytný)");
            put("sytost", "sytý (sytost, dosyta, nasytit, nenasytný)");
            put("dosyta", "sytý (sytost, dosyta, nasytit, nenasytný)");
            put("nasytit", "sytý (sytost, dosyta, nasytit, nenasytný)");
            put("nenasytný", "sytý (sytost, dosyta, nasytit, nenasytný)");
            put("sýr", "sýr (syreček, sýrař, sýrárna, syrovátka)");
            put("syreček", "sýr (syreček, sýrař, sýrárna, syrovátka)");
            put("sýrař", "sýr (syreček, sýrař, sýrárna, syrovátka)");
            put("sýrárna", "sýr (syreček, sýrař, sýrárna, syrovátka)");
            put("syrovátka", "sýr (syreček, sýrař, sýrárna, syrovátka)");
            put("syrový", "syrový (syrovinka)");
            put("syrovinka", "syrový (syrovinka)");
            put("sychravý", "sychravý (Sychrov)");
            put("Sychrov", "sychravý (Sychrov)");
            put("usychat", "usychat i usýchat (vysychat i vysýchat)");
            put("usýchat", "usychat i usýchat (vysychat i vysýchat)");
            put("vysychat", "usychat i usýchat (vysychat i vysýchat)");
            put("vysýchat", "usychat i usýchat (vysychat i vysýchat)");
            put("sýkora", "sýkora (sýkořice)");
            put("sýkořice", "sýkora (sýkořice)");
            put("sýček", "sýček");
            put("sysel", "sysel");
            put("syčet", "syčet (sykat, sykot)");
            put("sykat", "syčet (sykat, sykot)");
            put("sykot", "syčet (sykat, sykot)");
            put("sypat", DEF_SYP);
            put("sypký", DEF_SYP);
            put("sýpka", DEF_SYP);
            put("sypek", DEF_SYP);
            put("nasypat", DEF_SYP);
            put("násyp", DEF_SYP);
            put("násypný", DEF_SYP);
            put("osypaný", DEF_SYP);
            put("vysypat", DEF_SYP);
            put("zasypat", DEF_SYP);
            put("zásyp", DEF_SYP);
            put("Bosyně", "Bosyně");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_V = new HashMap<String, String>() {
        private static final long serialVersionUID = -3390024292231400979L;
        private final String DEF_VYSOKY = "vysoký (vysočina, Vysočany, vyšší, výše, výška, výšina, povýšit, vyvýšit, " +
                "vyvýšenina, zvýšit, převyšovat, Vyšehrad)";
        private final String DEF_ZVYK = "zvykat (zvyk, zlozvyk, zvyknout, zvyklost, navykat, navyknout, návyk, odvykat, " +
                "odvyknout, obvyklý)";

        {

            put("vy", "vy");
            put("vykat", "vykat");
            put("vysoký", DEF_VYSOKY);
            put("vysočina", DEF_VYSOKY);
            put("Vysočany", DEF_VYSOKY);
            put("vyšší", DEF_VYSOKY);
            put("výše", DEF_VYSOKY);
            put("výška", DEF_VYSOKY);
            put("výšina", DEF_VYSOKY);
            put("povýšit", DEF_VYSOKY);
            put("vyvýšit", DEF_VYSOKY);
            put("vyvýšenina", DEF_VYSOKY);
            put("zvýšit", DEF_VYSOKY);
            put("převyšovat", DEF_VYSOKY);
            put("Vyšehrad", DEF_VYSOKY);
            put("výt", "výt (zavýt)");
            put("zavýt", "výt (zavýt)");
            put("výskat", "výskat (výskot, zavýsknout)");
            put("výskot", "výskat (výskot, zavýsknout)");
            put("zavýsknout", "výskat (výskot, zavýsknout)");
            put("zvykat", DEF_ZVYK);
            put("zvyk", DEF_ZVYK);
            put("zlozvyk", DEF_ZVYK);
            put("zvyknout", DEF_ZVYK);
            put("zvyklost", DEF_ZVYK);
            put("navykat", DEF_ZVYK);
            put("navyknout", DEF_ZVYK);
            put("návyk", DEF_ZVYK);
            put("odvykat", DEF_ZVYK);
            put("odvyknout", DEF_ZVYK);
            put("obvyklý", DEF_ZVYK);
            put("žvýkat", "žvýkat (žvýkací, přežvykovat, přežvýkavec, žvýkačka)");
            put("žvýkací", "žvýkat (žvýkací, přežvykovat, přežvýkavec, žvýkačka)");
            put("přežvykovat", "žvýkat (žvýkací, přežvykovat, přežvýkavec, žvýkačka)");
            put("přežvýkavec", "žvýkat (žvýkací, přežvykovat, přežvýkavec, žvýkačka)");
            put("žvýkačka", "žvýkat (žvýkací, přežvykovat, přežvýkavec, žvýkačka)");
            put("vydra", "vydra (vydří, vydrovka, Povydří)");
            put("vydří", "vydra (vydří, vydrovka, Povydří)");
            put("vydrovka", "vydra (vydří, vydrovka, Povydří)");
            put("Povydří", "vydra (vydří, vydrovka, Povydří)");
            put("výr", "výr (pták)");
            put("vyžle", "vyžle");
            put("povyk", "povyk (povykovat)");
            put("povykovat", "povyk (povykovat)");
            put("výheň", "výheň");
            put("cavyky", "cavyky");
            put("vyza", "vyza");
            put("Vyškov", "Vyškov");
            put("Výtoň", "Výtoň");
        }
    };

    private static final Map<String, String> VYJMENOVANA_SLOVA_PO_Z = new HashMap<String, String>() {
        private static final long serialVersionUID = 8942549436708383986L;

        {
            put("brzy", "brzy");
            put("jazyk", "jazyk (jazýček, jazykozpyt, jazykověda, dvojjazyčný, jazylka)");
            put("jazýček", "jazyk (jazýček, jazykozpyt, jazykověda, dvojjazyčný, jazylka)");
            put("jazykozpyt", "jazyk (jazýček, jazykozpyt, jazykověda, dvojjazyčný, jazylka)");
            put("jazykověda", "jazyk (jazýček, jazykozpyt, jazykověda, dvojjazyčný, jazylka)");
            put("dvojjazyčný", "jazyk (jazýček, jazykozpyt, jazykověda, dvojjazyčný, jazylka)");
            put("jazylka", "jazyk (jazýček, jazykozpyt, jazykověda, dvojjazyčný, jazylka)");
            put("nazývat", "nazývat (vyzývat, vyzývavý, vzývat, ozývat se)");
            put("vyzývat", "nazývat (vyzývat, vyzývavý, vzývat, ozývat se)");
            put("vyzývavý", "nazývat (vyzývat, vyzývavý, vzývat, ozývat se)");
            put("vzývat", "nazývat (vyzývat, vyzývavý, vzývat, ozývat se)");
            put("ozývat se", "nazývat (vyzývat, vyzývavý, vzývat, ozývat se)");
            put("Ruzyně", "Ruzyně");
        }
    };

    private static final List<String> PREDPONY_S_Z = new ArrayList<String>() {
        private static final long serialVersionUID = -301362339120797706L;

        {
            add("zcestovat");
            add("scestovat");
            add("zkrápět");
            add("skrápět");
            add("zkontaktovat");
            add("skontaktovat");
            add("zestylizovat");
            add("sestylizovat");
        }
    };

    private static final Map<String, String> PREDPONY_S_Z_DUBLET = new HashMap<String, String>() {
        private static final long serialVersionUID = -4592730201638558625L;

        {
            put("sběh", "sběh (lidí) x (válečný) zběh");
            put("zběh", "sběh (lidí) x (válečný) zběh");
            put("sjednat", "sjednat (přednášku) x zjednat (nápravu)");
            put("zjednat", "sjednat (přednášku) x zjednat (nápravu)");
            put("správa", "správa (domu) x (novinová) zpráva");
            put("zpráva", "správa (domu) x (novinová) zpráva");
            put("zhlédnout", "zhlédnout (představení, film) x shlédnout (z rozhledny dolů)");
            put("shlédnout", "zhlédnout (představení, film) x shlédnout (z rozhledny dolů)");
            put("stěžovat si", "stěžovat si (ve škole) x ztěžovat (práci)");
            put("ztěžovat", "stěžovat si (ve škole) x ztěžovat (práci)");
        }
    };

    private static final List<String> PREJATA_SLOVA_S_Z = new ArrayList<String>() {
        private static final long serialVersionUID = -301362339120797706L;

        {
            add("kurz");
            add("kurs");
            add("diskurz");
            add("diskurs");
            add("exkurz");
            add("exkurs");
            add("impulz");
            add("impuls");
            add("pulz");
            add("puls");
            add("dispenz");
            add("dispens");
            add("komparz");
            add("kompars");
            add("konkurz");
            add("konkurs");
            add("rekurz");
            add("rekurs");
            add("reverz");
            add("revers");
            add("sukurz");
            add("sukurs");
        }
    };

    private static final List<String> PREJATA_SLOVA_S_Z_DVOJICE = new ArrayList<String>() {
        private static final long serialVersionUID = 9099923847450647350L;

        {
            add("analýza");
            add("analysa");
            add("azyl");
            add("asyl");
            add("báze");
            add("base");
            add("bazilika");
            add("basilika");
            add("buržoazie");
            add("buržoasie");
            add("cizelér");
            add("ciselér");
            add("dezaktivace");
            add("desaktivace");
            add("dezerce");
            add("deserce");
            add("dezert");
            add("desert");
            add("dezideratum");
            add("desideratum");
            add("deziluze");
            add("desiluse");
            add("dezinfekce");
            add("desinfekce");
            add("dezinformace");
            add("desinformace");
            add("dezintegrace");
            add("desintegrace");
            add("dezolátní");
            add("desolátní");
            add("dezorganizace");
            add("desorganisace");
            add("dezorientace");
            add("desorientace");
            add("dimenze");
            add("dimense");
            add("emulze");
            add("emulse");
            add("epizoda");
            add("episoda");
            add("exkurze");
            add("exkurse");
            add("exploze");
            add("explose");
            add("expozice");
            add("exposice");
            add("fantazie");
            add("fantasie");
            add("filozofie");
            add("filosofie");
            add("fyzika");
            add("fysika");
            add("fúze");
            add("fuse");
            add("geneze");
            add("genese");
            add("gnozeologie");
            add("gnoseologie");
            add("gymnázium");
            add("gymnasium");
            add("chromozom");
            add("chromosom");
            add("iluze");
            add("iluse");
            add("intenzita");
            add("intensita");
            add("invaze");
            add("invase");
            add("izobara");
            add("isobara");
            add("izoglosa");
            add("isoglosa");
            add("izolace");
            add("isolace");
            add("kazeta");
            add("kaseta");
            add("konzerva");
            add("konserva");
            add("konzervativní");
            add("konservativní");
            add("konzervatoř");
            add("konservatoř");
            add("konzul");
            add("konsul");
            add("konzulát");
            add("konsulát");
            add("konzultace");
            add("konsultace");
            add("konzumace");
            add("konsumace");
            add("koroze");
            add("korose");
            add("lyzol");
            add("lysol");
            add("muzeum ");
            add("museum");
            add("nervóza");
            add("nervosa");
            add("neuróza");
            add("neurosa");
            add("oáza");
            add("oasa");
            add("organizace");
            add("organisace");
            add("pauza");
            add("pausa");
            add("penze");
            add("pense");
            add("plazma");
            add("plasma");
            add("poezie");
            add("poesie");
            add("prézens");
            add("presens");
            add("prezentace");
            add("presentace");
            add("prezident");
            add("president");
            add("provize");
            add("provise");
            add("próza");
            add("prosa");
            add("revize");
            add("revise");
            add("rezignace");
            add("resignace");
            add("rezistence");
            add("resistence");
            add("rezoluce");
            add("resoluce");
            add("rezonátor");
            add("resonátor");
            add("rezultát");
            add("resultát");
            add("riziko");
            add("risiko");
            add("senzace");
            add("sensace");
            add("televize");
            add("televise");
            add("torzo");
            add("torso");
            add("tranzistor");
            add("transistor");
            add("trezor");
            add("tresor");
            add("tuberkulóza");
            add("tuberkulosa");
            add("univerzita");
            add("universita");
            add("úzus");
            add("usus");
            add("uzurpátor");
            add("usurpátor");
            add("verzálka ");
            add("versálka");
        }
    };

    private static final List<String> PREJATA_SLOVA_S_Z_IZMUS_ZMUS = new ArrayList<String>() {
        private static final long serialVersionUID = -3441139622962495322L;

        {
            add("neologismus");
            add("neologizmus");
            add("objektivismus");
            add("objektivizmus");
            add("realismus");
            add("realizmus");
            add("pleonasmus");
            add("pleonazmus");
            add("sarkasmus");
            add("sarkazmus");
            add("spasmus");
            add("spazmus");
            add("charisma");
            add("charizma");
            add("aneurysma");
            add("aneuryzma");
        }
    };

    private static final Map<String, String> DIS_DYS = new HashMap<String, String>() {
        private static final long serialVersionUID = -5466710428449774909L;

        {
            put("disharmonie", "disharmonie (nesoulad)");
            put("diskontinuita", "diskontinuita (nesouvislost, rozpojenost)");
            put("diskrepance", "diskrepance (neshoda, nepoměr)");
            put("dislokace", "dislokace (rozmístění, rozložení)");
            put("diskvalifikovat", "diskvalifikovat (vyřadit ze soutěže či závodu)");
            put("disonance", "disonance (nelibozvuk, neshoda, nesoulad)");
            put("disparita", "disparita (nerovnost)");
            put("disperze", "disperze (rozptyl, rozklad)");
            put("disproporce", "disproporce (nerovnoměrnost, nepoměr)");
            put("distinktivní", "distinktivní (rozlišovací)");
            put("distribuce", "distribuce (rozdělení)");
            put("disfunkce", "disfunkce (nežádoucí činnost nějakého prvku systému, který je v rozporu s jeho potřebami)");
            put("disfunkční", "disfunkční (nefunkční, neúčelný)");
            put("dyspepsie", "dyspepsie (porucha trávení)");
            put("dysfonie", "dysfonie (porucha hlasu)");
            put("dysfunkce", "dysfunkce (narušená, porušená nebo odchylná činnost některého orgánu)");
            put("dysgrafie", "dysgrafie (porucha psaní, neschopnost napodobit tvary písmen a naučit se správně psát)");
            put("dyslexie", "dyslexie (porucha čtení)");
            put("dyskalkulie", "dyskalkulie (porucha počítání)");
            put("dyslalie", "dyslalie (porucha výslovnosti, artikulace)");
            put("dysortografie", "dysortografie (porucha vnímání pravopisu)");
        }
    };

    private static final List<String> SPREZKY_A_SPRAHOVANI = new ArrayList<String>() {
        private static final long serialVersionUID = 3627430583586995427L;

        {
            add("na shledanou");
            add("na viděnou");
            add("na slyšenou");
            add("na rozloučenou");
            add("na odchodnou");
            add("na uvítanou");
            add("na zotavenou");
            add("na posilněnou");
            add("na pováženou");
            add("na uváženou");
            add("na rozmyšlenou");
            add("na vysvětlenou");
            add("na srozuměnou");
            add("na upřesněnou");
            add("na vybranou");
            add("na zavolanou");
            add("na rozdíl");
        }
    };

    private static final Map<String, String> ADJEKTIVA_ZAKONCENA_NI_NY = new HashMap<String, String>() {
        private static final long serialVersionUID = 3823515644413545461L;

        {
            put("adresní", "Adresní (např. štítek určený pro napsání adresy) " +
                    "× adresný (‚zcela konkrétní‘, např. výtka konkrétní osobě)");
            put("adresný", "Adresní (např. štítek určený pro napsání adresy) " +
                    "× adresný (‚zcela konkrétní‘, např. výtka konkrétní osobě)");
            put("čelní", "Čelní (od podstatného jména čelo, např. kost) " +
                    "× čelný (‚stojící v čele‘, např. představitel státu)");
            put("čelný", "Čelní (od podstatného jména čelo, např. kost) " +
                    "× čelný (‚stojící v čele‘, např. představitel státu)");
            put("dlužní", "Dlužní (‚potvrzující peněžní závazek‘, např. úpis) " +
                    "× dlužný (o tom, kdo je dlužen; o částce, kterou je třeba zaplatit)");
            put("dlužný", "Dlužní (‚potvrzující peněžní závazek‘, např. úpis) " +
                    "× dlužný (o tom, kdo je dlužen; o částce, kterou je třeba zaplatit)");
            put("dvorní", "Dvorní (od podst. jm. dvůr, např. dáma, etiketa) " +
                    "× dvorný (‚chovající se po dvorském způsobu, uhlazený‘)");
            put("dvorný", "Dvorní (od podst. jm. dvůr, např. dáma, etiketa) " +
                    "× dvorný (‚chovající se po dvorském způsobu, uhlazený‘)");
            put("jaderní", "Jaderní (např. o šťávě vyrobené z jader) × jaderný (‚nukleární‘)");
            put("jaderný", "Jaderní (např. o šťávě vyrobené z jader) × jaderný (‚nukleární‘)");
            put("mravní", "Mravní (‚vztahující se k mravům‘, např. zákony) × mravný (‚v souladu s dobrými mravy‘)");
            put("mravný", "Mravní (‚vztahující se k mravům‘, např. zákony) × mravný (‚v souladu s dobrými mravy‘)");
            put("prodejní", "Prodejní (např. stánek, v němž prodej probíhá) " +
                    "× prodejný (např. o zboží, které je určeno k dalšímu prodeji)");
            put("prodejný", "Prodejní (např. stánek, v němž prodej probíhá) " +
                    "× prodejný (např. o zboží, které je určeno k dalšímu prodeji)");
            put("samosprávní", "Samosprávní (vztahové k samospráva, např. úředník) " +
                    "× samosprávný (‚který sám sebe spravuje‘, např. celek, orgán)");
            put("samosprávný", "Samosprávní (vztahové k samospráva, např. úředník) " +
                    "× samosprávný (‚který sám sebe spravuje‘, např. celek, orgán)");
            put("srdeční", "Srdeční (‚vztahující se k srdci‘, např. tkáň, sval, záchvat) " +
                    "× srdečný (‚přátelský, vřelý‘)");
            put("srdečný", "Srdeční (‚vztahující se k srdci‘, např. tkáň, sval, záchvat) " +
                    "× srdečný (‚přátelský, vřelý‘)");
            put("trestní", "Trestní (‚vztahující se k trestu‘, např. právo) × trestný (‚postižitelný trestem‘)");
            put("trestný", "Trestní (‚vztahující se k trestu‘, např. právo) × trestný (‚postižitelný trestem‘)");
            put("volní", "Volní (‚založený na vůli‘: volní vlastnosti) × volný (‚svobodný, neomezený‘)");
            put("volný", "Volní (‚založený na vůli‘: volní vlastnosti) × volný (‚svobodný, neomezený‘)");
            put("zábavní", "Zábavní (‚určený k pobavení‘) × zábavný (‚přinášející pobavení‘)");
            put("zábavný", "Zábavní (‚určený k pobavení‘) × zábavný (‚přinášející pobavení‘)");
            put("zpěvní", "Zpěvní (řidč. ‚týkající se zpěvu, sloužící ke zpěvu‘, např. vlohy, ústrojí) " +
                    "× zpěvný (‚zpěvavý, melodický‘)");
            put("zpěvný", "Zpěvní (řidč. ‚týkající se zpěvu, sloužící ke zpěvu‘, např. vlohy, ústrojí) " +
                    "× zpěvný (‚zpěvavý, melodický‘)");
            put("živelní", "Živelní (od podst. jm. živel, např. pohroma) " +
                    "× živelný (‚nezkrotný, prudký, neovladatelný, nekoordinovaný‘; např. odboj, radost apod.)");
            put("živelný", "Živelní (od podst. jm. živel, např. pohroma) " +
                    "× živelný (‚nezkrotný, prudký, neovladatelný, nekoordinovaný‘; např. odboj, radost apod.)");
            put("životní", "Životní (vztahující se k životu, např. podmínky, projevy) " +
                    "× životný (ve významu ‚živý, živoucí‘)");
            put("životný", "Životní (vztahující se k životu, např. podmínky, projevy) " +
                    "× životný (ve významu ‚živý, živoucí‘)");

        }
    };

    private static final Map<String, String> ADJEKTIVA_ZAKONCENA_NI_NY_VYZNAMOVE_TOTOZNE = new HashMap<String, String>() {
        private static final long serialVersionUID = -3186062108375615419L;

        {
            put("skalní", "Skalní / řid. skalný (ve význ. ‚geologický útvar‘)");
            put("skalný", "Skalní / řid. skalný (ve význ. ‚geologický útvar‘)");
            put("niterný", "Niterný / řid. niterní");
            put("niterní", "Niterný / řid. niterní");
            put("obilní", "Obilní / obilný");
            put("obilný", "Obilní / obilný");
            put("odběrní", "Odběrní / řid. odběrný");
            put("odběrný", "Odběrní / řid. odběrný");
            put("žilní", "Žilní / žilný");
            put("žilný", "Žilní / žilný");
        }
    };

    private static final Map<String, String> ADJEKTIVA_ZAKONCENA_NI_NY_JINE_ZAKLADY = new HashMap<String, String>() {
        private static final long serialVersionUID = -3186062108375615419L;

        {
            put("nosní", "Nosní (od podstatného jména nos) × nosný (ke slovesu nosit)");
            put("nosný", "Nosní (od podstatného jména nos) × nosný (ke slovesu nosit)");
        }
    };

    //--------------

    private static final List<Character> KONSONANTY = new ArrayList<Character>() {
        private static final long serialVersionUID = -9025896333339463041L;

        {
            add('h');
            //add('ch');
            add('k');
            add('r');
            add('d');
            add('t');
            add('n');
            add('ž');
            add('š');
            add('č');
            add('ř');
            add('c');
            add('j');
            add('ď');
            add('ť');
            add('ň');
            add('b');
            add('f');
            add('l');
            add('m');
            add('p');
            add('s');
            add('v');
            add('z');
        }
    };

    private static final List<Character> VOKALY = new ArrayList<Character>() {
        private static final long serialVersionUID = -5283834541436824649L;

        {
            add('a');
            add('á');
            add('e');
            add('é');
            add('i');
            add('í');
            add('o');
            add('ó');
            add('u');
            add('ú');
            add('ů');
            add('y');
            add('ý');
        }
    };

    private static List<Character> SOUHLASKY_MEKKE = new ArrayList<Character>() {
        private static final long serialVersionUID = -8448191116229404359L;

        {
            add('b');
            add('f');
            add('m');
            add('l');
            add('p');
            add('s');
            add('v');
            add('z');
        }
    };

    private static List<Character> SOUHLASKY_OBOJETNE = new ArrayList<Character>() {
        private static final long serialVersionUID = -5168958512388483898L;

        {
            add('ž');
            add('š');
            add('č');
            add('ř');
            add('j');
            add('d');
            add('t');
            add('n');
        }
    };

    private static List<Character> SOUHLASKY_TVRDE = new ArrayList<Character>() {
        private static final long serialVersionUID = -9072492081551227855L;

        {
            add('h');
            add('k');
            add('r');
            add('d');
            add('t');
            add('n');
        }
    };

    private static List<Character> SOUHLASKY_MEKKE_A_OBOJETNE = new ArrayList<Character>() {
        private static final long serialVersionUID = -9072492081551227855L;

        {
            addAll(SOUHLASKY_MEKKE);
            addAll(SOUHLASKY_OBOJETNE);
        }
    };

    private static final Set<String> PREDPONY_SLOVESNE_A_OSTATNI = new HashSet<String>() {
        private static final long serialVersionUID = -8448191116229404359L;

        {
            add("bez");
            add("do");
            add("mimo");
            add("s");
            add("nej");
            add("pa");
            add("roz");
            add("ni");
            add("do");
            add("dů");
            add("na");
            add("nad");
            add("o");
            add("ob");
            add("od");
            add("po");
            add("pů");
            add("pod");
            add("pro");
            add("prů");
            add("pře");
            add("před");
            add("při");
            add("roz");
            add("sou");
            add("u");
            add("v");
            add("vy");
            add("vz");
            add("z");
            add("za");
            add("bůhví");
            add("čertví");
            add("čtvrti");
            add("kdoví");
            add("kolem");
            add("krom");
            add("lec");
            add("leda");
            add("lži");
            add("málo");
            add("mezi");
            add("mimo");
            add("místo");
            add("ně");
            add("ne");
            add("ni");
            add("nej");
            add("nevím");
            add("okolo");
            add("pa");
            add("polo");
            add("pra");
            add("pro");
            add("proti");
            add("přes");
            add("roz");
            add("skoro");
            add("sotva");
            add("spolu");
            add("vele");
            add("vně");
            add("znovu");
            add("euro");
        }
    };


    public CorrectorRulesStaticLists() {
    }

    public static Map<String, String> getVyjmenovanaSlovaPoBDublet() {
        return VYJMENOVANA_SLOVA_PO_B_DUBLET;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoLDublet() {
        return VYJMENOVANA_SLOVA_PO_L_DUBLET;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoMDublet() {
        return VYJMENOVANA_SLOVA_PO_M_DUBLET;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoPDublet() {
        return VYJMENOVANA_SLOVA_PO_P_DUBLET;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoSDublet() {
        return VYJMENOVANA_SLOVA_PO_S_DUBLET;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoVDublet() {
        return VYJMENOVANA_SLOVA_PO_V_DUBLET;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoZDublet() {
        return VYJMENOVANA_SLOVA_PO_Z_DUBLET;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoB() {
        return VYJMENOVANA_SLOVA_PO_B;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoL() {
        return VYJMENOVANA_SLOVA_PO_L;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoM() {
        return VYJMENOVANA_SLOVA_PO_M;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoP() {
        return VYJMENOVANA_SLOVA_PO_P;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoS() {
        return VYJMENOVANA_SLOVA_PO_S;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoV() {
        return VYJMENOVANA_SLOVA_PO_V;
    }

    public static Map<String, String> getVyjmenovanaSlovaPoZ() {
        return VYJMENOVANA_SLOVA_PO_Z;
    }

    public static List<String> getPredponySZ() {
        return PREDPONY_S_Z;
    }

    public static Map<String, String> getPredponySZDublet() {
        return PREDPONY_S_Z_DUBLET;
    }

    public static List<String> getPrejataSlovaSZ() {
        return PREJATA_SLOVA_S_Z;
    }

    public static List<String> getPrejataSlovaSZDvojice() {
        return PREJATA_SLOVA_S_Z_DVOJICE;
    }

    public static List<String> getPrejataSlovaSZIzmusZmus() {
        return PREJATA_SLOVA_S_Z_IZMUS_ZMUS;
    }

    public static Map<String, String> getDisDys() {
        return DIS_DYS;
    }

    public static List<String> getSprezkyASprahovani() {
        return SPREZKY_A_SPRAHOVANI;
    }

    public static Map<String, String> getAdjektivaZakoncenaNiNy() {
        return ADJEKTIVA_ZAKONCENA_NI_NY;
    }

    public static Map<String, String> getAdjektivaZakoncenaNiNyVyznamoveTotozne() {
        return ADJEKTIVA_ZAKONCENA_NI_NY_VYZNAMOVE_TOTOZNE;
    }

    public static Map<String, String> getAdjektivaZakoncenaNiNyJineZaklady() {
        return ADJEKTIVA_ZAKONCENA_NI_NY_JINE_ZAKLADY;
    }

    public static List<Character> getKONSONANTY() {
        return KONSONANTY;
    }

    public static List<Character> getVOKALY() {
        return VOKALY;
    }

    public static List<Character> getSouhlaskyMekke() {
        return SOUHLASKY_MEKKE;
    }

    public static List<Character> getSouhlaskyObojetne() {
        return SOUHLASKY_OBOJETNE;
    }

    public static List<Character> getSouhlaskyTvrde() {
        return SOUHLASKY_TVRDE;
    }

    public static List<Character> getSouhlaskyMekkeAObojetne() {
        return SOUHLASKY_MEKKE_A_OBOJETNE;
    }

    public static Set<String> getPredponySlovesneAOstatni() {
        return PREDPONY_SLOVESNE_A_OSTATNI;
    }
}
