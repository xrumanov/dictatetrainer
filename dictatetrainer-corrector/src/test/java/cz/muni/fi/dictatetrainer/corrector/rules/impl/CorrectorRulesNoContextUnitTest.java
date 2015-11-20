package cz.muni.fi.dictatetrainer.corrector.rules.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.model.MistakeType;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CorrectorRulesNoContextUnitTest {

    private static CorrectorRules cr;
    private Mistake mistake;

    @BeforeClass
    public static void setUp() {
        cr = new CorrectorRulesNoContext();
    }

    @Test
    public void vyjmenovanaSlovaB() {
        mistake = new Mistake(4, "y", "i", "dobytku", "dobitku", "Ty", "", 2, "dobytek", "k1gInSc3", "Ty dob(y)<i>tku!");

        String description = "být (bych, bys, by, bychom, byste, abych, abys, aby, …, kdybych, " +
                "kdybys, kdyby, …, bytí, živobytí, bývat, bývalý, byt, bytná, bytový, bytelný, bytost, bydlit, " +
                "bydliště, obydlí, bydlo (příbytek, živobytí), dobýt, dobyvatel, dobytek, dobytče, dobytkářství, " +
                "nabýt, nabývat, nábytek, obývat, obyvatel, obyvatelstvo, odbýt, odbyt, neodbytný, pozbýt, přebýt, " +
                "přebývat, přebytek, přibýt, přibývat, příbytek, ubýt, ubývat, úbytek, zbývat, zbytek, zabývat se) " +
                "Jde o vyjmenované nebo z cizího jazyka přejaté slovo. Tato slova mají společné psaní i/í " +
                "po souhláskách (b, f, m, l, p, s, v, z), které jsou tzv. obojetné, a ke každému ze zmíněných " +
                "písmen existuje seznam výjimek, u nichž se na místo i/í píše y/ý jako v tomto případě. " +
                "Důvod je historický a u každého slova jiný, je nutné si jej pamatovat. (VOJ)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.VYJMENOVANA_SLOVA)));
        assertThat(mistakeResult.getPriority(), is(equalTo(10)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void yjmenovanaSlovaBDublet() { // bytí - bití
        mistake = new Mistake(2, "y", "i", "bytí", "bití", "", "či", 1, "být", "k5eAaImFrD", "B(y)<i>tí či nebytí.");

        String description = "Ke slovesům být (= existovat) i bít (= tlouct) " +
                "tvoříme řadu předponových sloves (popř. přídavných jmen). Rozlišujeme:  být (ve škole) x bít (nepřítele)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.VYJMENOVANA_SLOVA)));
        assertThat(mistakeResult.getPriority(), is(equalTo(10)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vyjmenovanaSlovaL() { // pýchavku - píchavku
        mistake = new Mistake(2, "ý", "í", "pýchavku", "píchavku", "lese", "", 5,
                "pýchavka", "k1gFnSc4", "Našel jsem v lese p(ý)<í>chavku.");

        String description = "pýchavka (houba) x píchat (bodat)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.VYJMENOVANA_SLOVA)));
        assertThat(mistakeResult.getPriority(), is(equalTo(10)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vyjmenovanaSlovaM() { // smyčku - smičku
        mistake = new Mistake(3, "y", "i", "smyčku", "smičku", "chystají", "ve", 4,
                "smyčka", "k1gFnSc1", "Už mu chystají sm(y)<i>čku ve měste.");

        String description = "smýkat (smyk, smýčit, smyčec, smyčka, průsmyk) " +
                "Jde o vyjmenované nebo z cizího jazyka přejaté slovo. Tato slova mají společné " +
                "psaní i/í po souhláskách (b, f, m, l, p, s, v, z), které jsou tzv. obojetné, a ke " +
                "každému ze zmíněných písmen existuje seznam výjimek, u nichž se na místo i/í píše y/ý " +
                "jako v tomto případě. Důvod je historický a u každého slova jiný, je nutné si jej pamatovat. (VOJ)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.VYJMENOVANA_SLOVA)));
        assertThat(mistakeResult.getPriority(), is(equalTo(10)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vyjmenovanaSlovaFyto() { // fytoplankton - fitoplankton
        mistake = new Mistake(2, "y", "i", "fytoplankton", "fitoplankton", "žere", "", 2,
                "fytoplankton", "k1gInSc1", "Velryba žere f(y)<i>toplankton.");

        String description = "Slova s předponou fyto- patří mezi vyjmenovaná slova po f. " +
                "Jde o vyjmenované nebo z cizího jazyka přejaté slovo. Tato slova mají společné " +
                "psaní i/í po souhláskách (b, f, m, l, p, s, v, z), které jsou tzv. obojetné, a ke každému " +
                "ze zmíněných písmen existuje seznam výjimek, u nichž se na místo i/í píše y/ý jako v tomto případě. " +
                "Důvod je historický a u každého slova jiný, je nutné si jej pamatovat. (VOJ)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.VYJMENOVANA_SLOVA)));
        assertThat(mistakeResult.getPriority(), is(equalTo(10)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vyjmenovanaSlovaVy() { // výhodou - víhodou
        mistake = new Mistake(2, "ý", "í", "výhodou", "víhodou", "Jeho", "bylo", 2,
                "výhoda", "k1gFnSc7", "Jeho v(ý)<í>hodou bylo, že nebyl sám.");

        String description = "Slova s předponou vy-/vý- patří mezi vyjmenovaná slova po v. " +
                "Jde o vyjmenované nebo z cizího jazyka přejaté slovo. Tato slova mají společné psaní i/í " +
                "po souhláskách (b, f, m, l, p, s, v, z), které jsou tzv. obojetné, a ke každému ze zmíněných " +
                "písmen existuje seznam výjimek, u nichž se na místo i/í píše y/ý jako v tomto případě. " +
                "Důvod je historický a u každého slova jiný, je nutné si jej pamatovat. (VOJ)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.VYJMENOVANA_SLOVA)));
        assertThat(mistakeResult.getPriority(), is(equalTo(10)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void psaniYPoC() { // církve - cýrkve
        mistake = new Mistake(2, "í", "ý", "církve", "cýrkve", "do", "svatých", 3,
                "církev", "k1gFnSc1", "Vstoupil do c(í)<ý>rkve svatých sedmého dne.");

        String description = "Po c se v domácích slovech píše měkké i (např. cit, církev atd.). " +
                "U cizích slov pravidlo o tom, že se po c píše měkké i, neplatí. Na jedné straně máme slova jako citron, " +
                "nacista atp., na straně druhé máme slova, v nichž se po c píše tvrdé y – např. cyklon, cynik apod. " +
                "Pro psaní i a y v těchto slovech neexistuje žádné pravidlo, musíme si pamatovat jednotlivé příklady. (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.IY_po_C)));
        assertThat(mistakeResult.getPriority(), is(equalTo(10)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void psaniPredponSZ() { // zkontaktuji - skontaktuji
        mistake = new Mistake(1, "Z", "S", "Zkontaktuji", "Skontaktuji", "", "svoji", 1,
                "zkontaktovat", "k5eAaPmFrD", "(Z)<S>kontaktuji svoji mámu, neboť jsem šťastný.");

        String description = "NECHYBA U skupiny sloves mezi podobami s předponou s- a z- " +
                "zásadní významový rozdíl není, a můžete je psát oběma způsoby: " +
                "zcestovat i scestovat, zkrápět i skrápět... (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREDPONY_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void psaniPredponSZDublet() { // správa - zpráva
        mistake = new Mistake(1, "S", "Z", "Správa", "Zpráva", "", "", 1,
                "správa", "k5eAaPmFrD", "(S)<Z>práva domu.");

        String description = "Některá slova lze psát jak s předponou s-, tak s předponou z-. " +
                "U většiny z nich je mezi oběma podobami zřetelný významový rozdíl. (IJP) správa (domu) x (novinová) zpráva";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREDPONY_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void psaniPredponSZSkopirovat() { // zkopíroval - skopíroval
        mistake = new Mistake(1, "z", "s", "zkopíroval", "skopíroval", "", "", 4,
                "zkopírovat", "k5eAaPmAgInSrD", "V práci si (z)<s>kopíroval tajné dokumenty.");

        String description = "U sloves zakončených na -ovat (aktualizovat, kopírovat) je podoba s " +
                "předponou z- vždy správná. Jen u některých z nich lze zvolit i předponu s- (zestylizovat i " +
                "sestylizovat, zkontaktovat i skontaktovat). Výjimkou jsou pouze ta slovesa, která mají s- už v " +
                "původním jazyku: skandalizovat (neexistuje samotné kandalizovat), skandovat. (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREDPONY_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void psaniPredponSZOthers() { // zocelí - socelí
        mistake = new Mistake(1, "z", "s", "zocelí,", "socelí,", "Válka", "nebo", 3,
                "zocelit", "k5eAaPmAgInSrD", "Válka člověka buď (z)<s>ocelí, nebo zabije.");

        String description = "Předpona s(e)- naznačuje a) směřování dohromady: " +
                "scelit, shromáždit se, spojit; b) směřování shora dolů, po povrchu pryč: sklonit, sklopit, shýbnout se;" +
                " c) zmenšení objemu, až zánik (scvrknout se, shořet). U mnohých slov je třeba si způsob psaní pamatovat, " +
                "neboť původní význam si dnes již neuvědomujeme: skončit, slevit (sleva).Předponou z(e)- se tvoří: " +
                "a) od nedokonavých sloves dokonavá, která nemají žádný z významů (moknout › zmoknout); " +
                "b) od podstatných nebo přídavných jmen slovesa mající význam ‚učinit nebo stát se tím, " +
                "co znamená slovo základové‘: ocel › zocelit. U mnohých slov si je třeba způsob psaní pamatovat: " +
                "zkoumat, zkoušet (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREDPONY_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void predlozkySZ() { // z - s
        mistake = new Mistake(1, "z", "s", "z", "s", "Vstal", "postele.", 2,
                "z", "k7c2", "Vstal (z)<s> postele");

        String description = "S druhým pádem se pojí předložka z (ze). " +
                "Např. vstal z postele; vyšel z banky. Se sedmým pádem se pojí jedině předložka s (se). " +
                "Např. trávil čas s knihou; s pilinami si lze hrát. (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREDLOZKY_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void prejataSlovaSeSZ() { // konkurz - konkurs
        mistake = new Mistake(7, "z", "s", "konkurz", "konkurs", "Šla", "modelek.", 3,
                "konkurs", "k1gInSc1", "Šla na konkur(z)<s> modelek.");

        String description = "NECHYBA Ve slovech zakončených v 1. pádě na skupinu vyslovovanou " +
                "[-ns, -rs, -ls, např. kurs], v ostatních pádech a ve slovech odvozených vyslovovanou " +
                "[-nz-, -rz-, -lz-, např. bez kurzu], jsou obě možnosti psaní rovnocenné, " +
                "a to bez stylového rozlišení. (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREJATA_SLOVA_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void prejataSlovaSeSZzdomacnele() { // azyl - asyl
        mistake = new Mistake(2, "z", "s", "azyl", "asyl", "žadateli", "", 7,
                "azyl", "k1gInSc1", "Německo má problémy se žadateli o a(z)<s>yl.");

        String description = "Ve slovech zdomácnělých, " +
                "kde se původní s vždy v češtině vyslovuje jako [z], se podoby se z považují za základní, " +
                "tedy stylově neutrální. Podoby se s jsou stylově příznakové, proto se užívají ve specifických " +
                "(např. úzce odborných) textech. Např.: analýza – analysa, azyl – asyl";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREJATA_SLOVA_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void prejataSlovaSeSZizmus() { // sarkazmu - sarkasmu
        mistake = new Mistake(6, "z", "s", "sarkazmu", "sarkasmu", "detektor", "", 4,
                "sarkasmus", "k1gInSc1", "Kéžby vynalezli detektor sarka(s)<z>mu!");

        String description = "Ve slovech s příponou vyslovovanou jako [-izmus]/[-zmus] " +
                "a [-zmus]/[-zma] můžeme psát s i z, přičemž podoba se s se považuje za základní. (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PREJATA_SLOVA_S_Z)));
        assertThat(mistakeResult.getPriority(), is(equalTo(7)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void psaniDisDys1() { // disfunkční - dysfunkční, dysfunkce - disfunkce

        mistake = new Mistake(2, "i", "y", "Disfunkční", "Dysfunkční", "", "orgán", 1,
                "disfunkční", "k2eAgFnPc1d1", "D(i)<y>sfunkční orgán někdy vede až k jeho úplné d(y)<i>sfunkci.");

        String description1 = "Významově poměrně blízké předpony dis- a dys- mají rozdílný původ. " +
                "O jejich užití obvykle rozhoduje tradice. Latinské dis- odpovídá české předponě roz-, " +
                "popř. ne-. Řecké dys- znamená ‚zeslabený, vadný, porušený‘, předpona vyjadřuje něco negativního, " +
                "špatného. (IJP) disfunkční (nefunkční, neúčelný)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.DIS_DYS)));
        assertThat(mistakeResult.getPriority(), is(equalTo(8)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description1)));

    }

    @Test
    public void psaniDisDys2() { // disfunkční - dysfunkční, dysfunkce - disfunkce

        mistake = new Mistake(6, "y", "i", "dysfunkci", "disfunkci", "úplné", "", 9,
                "dysfunkce", "k1gFnPc1", "D(i)<y>sfunkční orgán někdy vede až k jeho úplné d(y)<i>sfunkci.");

        String description = "Významově poměrně blízké předpony dis- a dys- mají rozdílný původ. " +
                "O jejich užití obvykle rozhoduje tradice. Latinské dis- odpovídá české předponě roz-, " +
                "popř. ne-. Řecké dys- znamená ‚zeslabený, vadný, porušený‘, předpona vyjadřuje něco negativního, " +
                "špatného. (IJP) " +
                "dysfunkce (narušená, porušená nebo odchylná činnost některého orgánu)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.DIS_DYS)));
        assertThat(mistakeResult.getPriority(), is(equalTo(8)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void slovaZakončenéManie() { // cestománie - cestomanie

        mistake = new Mistake(7, "á", "a", "cestománie", "cestomanie", "jeho", "se", 3,
                "cestománie", "k1", "Z jeho cestom(á)<a>nie se stala m(á)<a>nie.");


        String description = "NECHYBA Ve složeninách obsahujících -manie je možné psát -mánie i -manie. (VOJ)";


        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.SLOVA_ZAKONCENE_MANIE)));
        assertThat(mistakeResult.getPriority(), is(equalTo(6)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));

    }

    @Test
    public void slovaZakončenéManie1() { // mánie - manie

        mistake = new Mistake(2, "á", "a", "mánie.", "manie.", "stala", "", 6,
                "mánie", "k1gFnPc1", "Z jeho cestom(á)<a>nie se stala m(á)<a>nie.");

        String description = "Přípustná je pouze varianta mánie. Pouze pokud jde o slovo složené " +
                "(např. cestománie, pokemanie) je možné psát krátce i dlouze. (VOJ)";

        Mistake mistakeResult2 = cr.applyRules(mistake);

        assertThat(mistakeResult2.getMistakeType(), is(equalTo(MistakeType.SLOVA_ZAKONCENE_MANIE)));
        assertThat(mistakeResult2.getPriority(), is(equalTo(6)));
        assertThat(mistakeResult2.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void psaniNaNn() { // denního - deního
        mistake = new Mistake(4, "n", "", "denního", "deního", "jeho", "rituálu", 7,
                "denní", "k2eAgFnPc1d1", "Káva a cigareta byla součástí jeho den(n)ího rituálu.");

        String description = "Dvě n se píšou u přídavných jmen odvozených příponou -ní, -ný od podstatných jmen, " +
                "jejichž kořen končí na -n,, -ň (den – denní, holeň – holenní). Dvě n se píšou i u přídavných jmen " +
                "(a u výrazů od nich odvozených), u nichž výchozí podstatné jméno neexistuje nebo se neužívá, " +
                "ale kořen je zakončen na -n (bezcenný (bezcennost)). Píše se také v druhých a třetích stupních " +
                "přídavných jmen a slovech od nich odvozených." +
        "Jedno n se píše v případech:" +
        " a) u přídavných jmen odvozených příponou -í od jmen pojmenovávajících zvířata, " +
                "jejichž kořen končí na -n: (havran – havraní), a od příčestí trpných: dán – daný;" +
        " b) slovo raný ve významu ,brzký, časný‘ (raná gotika) a od r. 1957 též dceřiný;" +
        " c) u přídavných jmen utvořených od podstatných jmen příponou -ěný: vlna – vlněný (stejně jako sláma – slaměný);" +
        "d) podstatná jména odvozená od přídavných jmen na -ní, -ný příponami -ík, -ice, -ina: deník, okenice, cenina. (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.PSANI_N_NN)));
        assertThat(mistakeResult.getPriority(), is(equalTo(5)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void sprezkySprahovaniZaTo() { // za to - zato
        mistake = new Mistake(-1, " ", "", "Za to", "Zato", "", "já", 1,
                "NOT_FOUND", "NOT_FOUND", "Za()to já nemůžu.");

        String description = "Příslovce zato užíváme ve významu ‚náhradou za něco, místo něčeho‘ " +
                "(Vrátil se s nepořízenou, zato rozčílený.). Zato také funguje ve významu spojky odporovací, " +
                "synonymickým výrazem je však: Učit se nechtěl, zato se uměl prát. V ostatních případech píšeme " +
                "výraz za to zvlášť: Mám za to, že je schůze už zbytečně dlouhá. (IJP)";

        Mistake mistakeResult = cr.applyRules(mistake);

        assertThat(mistakeResult.getMistakeType(), is(equalTo(MistakeType.SPREZKY_SPRAHOVANI)));
        assertThat(mistakeResult.getPriority(), is(equalTo(5)));
        assertThat(mistakeResult.getMistakeDescription(), is(equalTo(description)));

    }

    @Test
    public void sprezkySprahovaniNaTo() { // nato - na to

        mistake = new Mistake(0, "", " ", "nato", "na to", "brzy", "zemřel.", 5,
                "NOT_FOUND", "NOT_FOUND", "Vypil láhev vitriolu, brzy na<>to zemřel.");


        String description = "Příslovce nato znamená ‚potom, poté‘: Vypil láhev vitriolu, brzy nato zemřel. " +
                "Hned nato začala předstírat, že se nic neděje. Zvlášť píšeme spojení předložky a zájmena: " +
                "Jdeme na to. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SPREZKY_SPRAHOVANI)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void sprezkySprahovaniPoTom() { // po tom - potom

        mistake = new Mistake(-1, " ", "", "po tom", "potom", "a", "zazpíval.", 3,
                "NOT_FOUND", "NOT_FOUND", "Hrál a po()tom zazpíval.");


        String description = "Zde není vymezení tak jednoznačné. Oslabuje se totiž chápání tohoto výrazu jako spojení " +
                "předložky a ukazovacího zájmena a ve většině textů lze volbu odůvodnit jak významem " +
                "‚zároveň‘ (přitom), tak vztáhnutím na děj předchozí věty, ke kterému zájmeno odkazuje (při tom)." +
                "Podobný případ je předložka + zájmeno po tom a příslovce potom. Hrál a potom zpíval (‚následovně‘). " +
                "Hrál a po tom zpíval. (= Po hraní na hudební nástroj začal zpívat.) (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SPREZKY_SPRAHOVANI)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void sprezkySprahovaniPote() { // pote - po te

        mistake = new Mistake(0, "", " ", "poté", "po té", "a", "půjdeme.", 5,
                "NOT_FOUND", "NOT_FOUND", "Dáme si čaj a po<>té půjdeme spát.");


        String description = "Pokud ukazovací zájmeno odkazuje k entitě ženského rodu, jsou ve většině případů možné " +
                "obě varianty: Jako předkrm si dáme šunku a po té (‚po šunce‘) hlavní chod. Jako předkrm si dáme šunku " +
                "a poté (‚následovně‘) hlavní chod. Kde však tato situace nenastává, tj. pokud odkazujeme k " +
                "jinému rodu, je možná pouze jednoslovná podoba – Dáme si čaj a poté (v časovém významu – " +
                "‚až dopijeme čaj‘) půjdeme spát. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SPREZKY_SPRAHOVANI)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void sprezkySprahovaniNaShle() { // na shledanou - nashledanou

        mistake = new Mistake(-1, " ", "", "na shledanou", "nashledanou", "Řekl", "a", 2,
                "NOT_FOUND", "NOT_FOUND", "Řekl na<>shledanou a už se nevrátil.");


        String description = "Zvlášť píšeme zpodstatnělá přídavná jména, která jsou užívaná pouze či převážně s " +
                "předložkou na a pojí se s 4. pádem. Jsou to spojení jako: na shledanou, na viděnou apod. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SPREZKY_SPRAHOVANI)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void slozenaAdjektiva() { // politicko-ekonomická - politickoekonomická

        mistake = new Mistake(-1, "-", "", "Politicko-ekonomická", "Politickoekonomická", "", "situace", 1,
                "NOT_FOUND", "NOT_FOUND", "Politicko(-)ekonomická situace evropy je nedobrá.");


        String description = "V těchto případech používáme pro oddělení obou složek spojovník (ten zde v podstatě " +
                "plní funkci spojky a). Např.: zemědělsko-potravinářský (týkající se zemědělství a potravinářství). " +
                "Někdy je užíván k rozlišení: politicko-ekonomický (souřadicí; týkající se politiky a ekonomie) a " +
                "politickoekonomický (podřadicí; týkající se politické ekonomie) (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SLOZENA_ADJEKTIVA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

}


