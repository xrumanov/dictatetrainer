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

        String description = "dobytek Jde o vyjmenované nebo z cizího jazyka přejaté slovo. Tato slova mají " +
                "společné psaní i/í po souhláskách (b, f, m, l, p, s, v, z), které jsou tzv. obojetné, a ke " +
                "každému ze zmíněných písmen existuje seznam výjimek, u nichž se na místo i/í píše y/ý jako v " +
                "tomto případě. Důvod je historický a u každého slova jiný, je nutné si jej pamatovat. (VOJ)";

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
        mistake = new Mistake(1, "S", "Z", "Správa", "Zpráva", "", "domu.", 1,
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
                "z", "k7c2", "Vstal (z)<s> postele.");

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
        mistake = new Mistake(-3, " ", "", "Za to", "Zato", "", "já", 1,
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

        mistake = new Mistake(-3, " ", "", "po tom", "potom", "a", "zazpíval.", 3,
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

        mistake = new Mistake(-3, " ", "", "na shledanou", "nashledanou", "Řekl", "a", 2,
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

        mistake = new Mistake(-10, "-", "", "Politicko-ekonomická", "Politickoekonomická", "", "situace", 1,
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

    @Test
    public void velkaPismenaZacatekVety() { // Vyšel - vyšel

        mistake = new Mistake(1, "V", "v", "Vyšel", "vyšel", "", "ven.", 1,
                "vyjít", "k5", "(V)<v>yšel ven.");


        String description = "Věta vždy začíná velkým písmenem.";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VELKA_PISMENA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void velkaPismenaChyba() { // Moravě - moravě

        mistake = new Mistake(1, "M", "m", "Moravě.", "moravě.", "na", "", 3,
                "morava", "k1", "Bydlím na (M)<m>oravě.");


        String description = "Názvy jednoslovné (např. jméno Miloš, město Kladno) začínají velkým písmenem. " +
                "U víceslovných názvů (např. Mrtvé moře) se píše jako velké pouze první písmeno, pokud už " +
                "neobsahují další název (např. Ústí nad Labem, řeka Labe). K těmto základním pravidlům však " +
                "existuje množství dodatků (k nalezení např. v Pravidlech českého pravopisu), např. o " +
                "psaní názvů ulic, živých bytostí, institucí atd. (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VELKA_PISMENA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void slovaZakoncenaNaIci() { // Řídící - Řídicí

        mistake = new Mistake(4, "í", "i", "Řídící", "Řídicí", "", "žena", 1,
                "řídící", "k2", "Říd(í)<i>cí žena je žena, která právě řídí.");


        String description = "Podoby typu měřicí, řídicí jsou přídavná jména účelová. Vyjadřují, k čemu věc slouží: " +
                "měřicí zařízení je zařízení určené k měření, řídicí (ovládací) panel je pojmenován podle toho, že " +
                "slouží k řízení (ovládání). Přídavná jména typu měřící, řídící se nazývají dějová. Jimi se vyjadřuje " +
                "děj (činnost) právě probíhající: např. řídící žena je žena, která právě řídí. " +
                "U osob se podoby na -ící používají výhradně: řídící pracovník, velící důstojník, místodržící." +
                "Pomůcka: Do daného spojení dosadit jeden člen z dvojic typu: vzdělávací – vzdělávající. " +
                "Je-li vzdělávací středisko, mělo by být i školicí středisko, je-li útvar provádějící činnost, " +
                "pak je i útvar řídící činnost. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.ADJEKTIVA_ICI)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void slovaZakoncenaNaNiRozlisneVyznamy() { // Čelný - Čelní

        mistake = new Mistake(5, "ý", "í", "Čelný", "Čelní", "", "představitel", 1,
                "čelný", "k2", "Čeln(ý)<í> představitel komunistické strany se včera otočil v hrobě.");


        String description = "Nevelká skupina jmen, u nichž si -ní a -ný buď konkurují, " +
                "nebo slouží k rozlišení různých významů. Zakončení na -ný mají přídavná jména s významem " +
                "hodnotícím, proto např. spojením zábavný pořad vyjadřujeme svůj názor na charakter pořadu," +
                " hodnotíme jej, kdežto jako zábavní pořad může být tvůrci označen i pořad, který diváci po " +
                "zhlédnutí možná ani za zábavný považovat nebudou. (IJP) Čelní (od podstatného jména čelo, např. kost) " +
                "× čelný (‚stojící v čele‘, např. představitel státu)";
        ;

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.ADJEKTIVA_NI_NY)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void slovaZakoncenaNaNiNechyba() { // Obilný - Obilní

        mistake = new Mistake(6, "ý", "í", "Obilný", "Obilní", "zastávke", "trh.", 4,
                "obilní", "k2", "Vystoupil na zastávke Obiln(ý)<í> trh.");


        String description = "NECHYBA Významově totožné varianty. Obilní / obilný";
        ;

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.ADJEKTIVA_NI_NY)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void slovaZakoncenaNaNiNosný() { // nosný - nosní

        mistake = new Mistake(5, "ý", "í", "nosný.", "nosní.", "je", "", 6,
                "nosný", "k2", "Ten trám nemužeš vyhodit, je nosn(ý)<í>.");


        String description = "Tato jména jsou tvořena od různých základů slov." +
                "Nosní (od podstatného jména nos) × nosný (ke slovesu nosit)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.ADJEKTIVA_NI_NY)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void typAckoliKdokoli() { // kdokoliv - kdokoli

        mistake = new Mistake(-8, "v", "", "kdokoliv.", "kdokoli.", "udělat", "", 4,
                "kdokoliv", "k6", "To mohl udělat kdokoli(v).");


        String description = "NECHYBA Zájmena jako např. kdokoli(v), cokoli(v) atd., příslovce kdykoli(v), " +
                "kamkoli(v), částice nikoli(v) a spojka ačkoli(v) mohou mít dvě zcela rovnocenné podoby zakončení: " +
                "-koli a -koliv. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.TYP_ACKOLI_ACKOLIV)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vokalizacePredlozekStejnyZacatek() { // ke - k

        mistake = new Mistake(-2, "e", "", "ke", "k", "spát", "kamáradovi", 3,
                "ke", "k7", "Jdu spát k(e) kamáradovi.");


        String description = "Před slovem, které začíná stejnou souhláskou jako předložka, vokalizujeme vždy." +
                " Např.: ke kořenům, se sestrou. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VOKALIZACE_PREDLOZEK)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vokalizacePredlozekPodobnyZacatek() { // ve - v

        mistake = new Mistake(-2, "e", "", "ve", "v", "si", "Finsku", 3,
                "ve", "k7", "Koupil si v(e) Finsku vodku.");


        String description = "Tendence k vokalizaci je silná, pokud předložka stojí před slovem začínajícím " +
                "podobnou souhláskou, tedy s před z-, ž-, š-; z před s-, š-, ž-; v před f-; k před g-. " +
                "Např.: ke groši, se zemí, ve Finsku. U některých slov však pravidlo nemusí platit " +
                "(Např.: nevycházím z šoku). Důležitou roli zde hraje právě faktor výslovnosti. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VOKALIZACE_PREDLOZEK)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vokalizacePredlozekNevokalizace() { // k - ke

        mistake = new Mistake(0, "", "e", "k", "ke", "dodatek", "ústavě.", 3,
                "ústava", "k1", "Osumnáctý dodatek k<e> ústavě.");


        String description = "Před slovem začínajícím samohláskou se neslabičné předložky nevokalizují " +
                "(z \"k\" se nestane \"ke\" atp.). Např.: k ústavě, s ementálem. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VOKALIZACE_PREDLOZEK)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vokalizacePredlozekSeSebou() { // s sebou - se sebou

        mistake = new Mistake(0, "", "e", "s", "se", "prosím", "sebou.", 5,
                "s", "k7", "Bude pršet, deštníky prosím s<e> sebou.");


        String description = "Výjimka, jde o ustálené spojení. Vyskytuje se pouze v instrumentálu. Např. brát s sebou," +
                " vzít s sebou. Sebou vyjadřuje pohyb těla (mrskat sebou, …). (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VOKALIZACE_PREDLOZEK)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vokalizacePredlozekJednaSouhlaska() { // k řece - ke řece

        mistake = new Mistake(0, "", "e", "k", "ke", "napít", "řece.", 5,
                "k", "k7", "Jelen se šel napít k<e> řece.");


        String description = "Před jednou souhláskou se neslabičné předložky nevokalizují. " +
                "Např.: k řece, s pentličkami. (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VOKALIZACE_PREDLOZEK)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void vokalizacePredlozekRetneSouhlasky() { // ku - k

        mistake = new Mistake(-2, "u", "", "ku", "k", "to", "prospěchu", 3,
                "nosný", "k2", "Vyřešil to k(u) prospěchu věci.");


        String description = "Ku se dochovalo pouze v některých ustálených spojeních, a to jen před slovy " +
                "začínajícími retnou souhláskou, zejména p (např. ku příkladu, ku prospěchu) a ve " +
                "vyjadřování poměrnosti (např. pět ku sedmi). (IJP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.VOKALIZACE_PREDLOZEK)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void zajmenaJiNi() { // ni - ní

        mistake = new Mistake(2, "i", "í", "ni.", "ní.", "o", "", 3,
                "ona", "k3", "Nestojím o n(i)<í>.");


        String description = "Krátké varianty ji/ni, vaši/naši apod. jsou vázány pouze ke 4. pádu, " +
                "ostatní pády jsou jí/ní vaší/naší. Pomůcka: Za dané zájmeno dosadit ukazovací zájmena té, " +
                "tou a tu. Pokud se do věty hodí zájmeno tu, patří sem ni, ji " +
                "(Nestojím o ni. -> Nestojím o tu dívku.). V ostatních případech (té, tou) sem patří jí, " +
                "ní (Šli bez ní. -> Šli bez té dívky.). (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.ZAJMENA_VASI_JI_NI)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    //    p = "Psaní bě/bje, vě/vje, pě"
//            (0,j)
//    následující chyba v tomto slově je (e,ě) AND jedno z {b, v, p} předchází chybě:
//            >> "Skupiny bje a vje píšeme u těch slov, která jsou tvořena předponami ob- a v-, přičemž kořen slova začíná na je-. (ob-jezdit – objezd; v-jet – vjezd). U ostatních slov píšeme ě (oběd, pěnkava, větev). V žádném českém slovu se nevyskytuje pjě. Řídkou výjimkou jsou některé názvy, např. Skopje." (VOJ) AND další chybu v tomto slově ignorovat
    @Test
    public void psaniBeVePe() { // objezd - obězd

        mistake = new Mistake(3, "je", "ě", "objezd.", "obězd.", "kruhový", "", 5,
                "objezd", "k1", "Ve měste vybudovali kruhový ob(je)<ě>zd.");


        String description = "Skupiny bje a vje píšeme u těch slov, která jsou tvořena předponami ob- a v-, " +
                "přičemž kořen slova začíná na je-. (ob-jezdit – objezd; v-jet – vjezd). " +
                "U ostatních slov píšeme ě (oběd, pěnkava, větev). V žádném českém slovu se nevyskytuje pjě. " +
                "Řídkou výjimkou jsou některé názvy, např. Skopje. (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.PSANI_BE_VE_PE)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void souhlaskyParoveZnelostCh() { // potah - potach

        mistake = new Mistake(5, "h", "ch", "potah.", "potach.", "lososový", "", 4,
                "potah", "k1", "Vybrala si lososový pota(h)<ch>.");


        String description = "Souhlásky rozdělujeme na znělé (b, d, ď, g, h, v, z, ž) a " +
                "neznělé (p, t, ť, k, ch, f, s, š). Na konci slov je každá souhláska vyslovována nezněle. " +
                "Pomůcka: Říci si slovo v jiném tvaru. Např. potah zní jako [potach], jiný tvar: bez potahu. (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SOUHLASKY_PAROVE)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void souhlaskyParoveZnelost() { // proud - prout

        mistake = new Mistake(4, "d", "t", "proud.", "prout.", "Nejde", "", 2,
                "proud", "k1", "Nejde prou(d)<t>.");

        String description = "Souhlásky rozdělujeme na znělé (b, d, ď, g, h, v, z, ž) a " +
                "neznělé (p, t, ť, k, ch, f, s, š). Na konci slov je každá souhláska vyslovována nezněle. " +
                "Pomůcka: Říci si slovo v jiném tvaru. Např. potah zní jako [potach], jiný tvar: bez potahu. (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SOUHLASKY_PAROVE)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void souhlaskyParoveSkupina() { // svatbu - svadbu

        mistake = new Mistake(4, "t", "d", "svatbu.", "svadbu.", "sestřinu", "", 4,
                "svatba", "k1", "Šla na sestřinu sva(t)<d>bu.");

        String description = "Skupina párových souhlásek se vyslovuje celá buď zněle, nebo nezněle, " +
                "ale označování jednotlivých hlásek se řídí podle toho, které hlásky jsou v jiných tvarech " +
                "téhož slova nebo ve slovech příbuzných, např. vézt (vezu), kavka (kavek), sjezd (sjezdu), " +
                "lehký (lehounký). (PČP)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.SOUHLASKY_PAROVE)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void diakritika() { // ústa - ůsta

        mistake = new Mistake(1, "ú", "ů", "ústa", "ůsta", "A", "chvějí", 2,
                "ústa", "k1", "A (ú)<ů>sta se chvějí.");

        String description = "Na začátku slova se dlouhé u vždy vyjadřuje jako ú. (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.DIAKRITIKA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void diakritikaUhelnik() { // trojúhelník - trojuhelník

        mistake = new Mistake(5, "ú", "u", "trojúhelník!", "trojuhelník!", "není", "", 3,
                "trojúhelník", "k1", "To není troj(ú)<u>helník!");

        String description = "Slovo složené z více kořenů (např. troj + úhelník) si ú uchovává i uvnitř slova. (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.DIAKRITIKA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void diakritikaPrejataSlova() { // múza - můza

        mistake = new Mistake(2, "ú", "ů", "múza.", "můza.", "moje", "", 6,
                "můza", "k1", "Dnes o šesté přijde moje m(ů)<ú>za.");

        String description = "Jde o přejaté slovo (ocún, múza), které se takto odlišuje od slov " +
                "domácích (kůlna, důl, …). Ú se vyskytuje také u citoslovcí (hú, vrkú). (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.DIAKRITIKA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void diakritikaNeprejataSlova() { // kůlně - kúlně

        mistake = new Mistake(5, "ů", "ú", "kůlně", "kúlně", "V", "našel", 2,
                "kůlna", "k1", "V k(ů)<ú>lně našel staré kolo.");

        String description = "U nepřejatých slov (např. kůlna, důl) je dlouhé u, " +
                "které není na začátku slova, vyjádřeno jako ů.";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.DIAKRITIKA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void DTNzanikHacku() { // nedělat - neďělat

        mistake = new Mistake(3, "d", "ď", "nedělat", "neďělat", "je", "nic.", 3,
                "nedělat", "k5", "Nejhorší je ne(ď)<d>ělat nic.");

        String description = "Háček (tzv. klička) zaniká, i přes odlišnou výslovnost. Měkkost přechází na samohlásku " +
                "e (stane se z ní ě, např.: dělat) nebo i (např.: dílna). (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.DIAKRITIKA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void DTNuchovaniHacku() { // Ďábel - Dábel

        mistake = new Mistake(1, "Ď", "D", "Ďábel", "Dábel", "", "obchází", 1,
                "ďábel", "k1", "(Ď)<D>ábel obchází mezi námi.");

        String description = "U ď, ť, ň na konci slova dochází k uchování háčku " +
                "(např.: loď, choť). Stejně tak, pokud následují samohlásky a, o, u. (např.: ďábel) (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.DIAKRITIKA)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void IpoObojetnychSouhlaskach() { // víno - výno

        mistake = new Mistake(2, "í", "ý", "víno,", "výno,", "Pijeme", "chceš", 2,
                "víno", "k1", "Pijeme v(í)<ý>no, chceš se přidat?");


        String description = "Tato slova mají společné psaní i/í po souhláskách (b, f, m, l, p, s, v, z), " +
                "které jsou tzv. obojetné, a ke každému ze zmíněných písmen existuje seznam výjimek " +
                "(tzv. vyjmenovaná slova), u nichž se na místo i/í píše y/ý (např.: východ, pýcha, bylina). " +
                "Y/ý se píše také po některých přejatých slovech. (Např.: fyziognomie, zygota). (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.I_PO_MEKKYCH_OBOJETNYCH_SOUHLASKACH)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void IpoMekkychSouhlaskach() { // šíku - šýku

        mistake = new Mistake(2, "í", "ý", "šíku.", "šýku.", "jednom", "", 5,
                "šík", "k1", "Pochodovali všichni v jednom š(í)<ý>ku.");


        String description = "Patří mezi pravopisně měkké souhlásky (ž, š, č, ř, c, j, ď, ť, ň)," +
                " které jsou vždy následovány i/í.";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.I_PO_MEKKYCH_OBOJETNYCH_SOUHLASKACH)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void IpoTvrdychSouhlaskach() { // kýžený - kížený

        mistake = new Mistake(2, "ý", "í", "kýžený", "kížený", "Dosáhl", "výsledek.", 2,
                "kýžený", "k2", "Dosáhl k(ý)<í>žený výsledek.");


        String description = "Patří mezi pravopisně tvrdé souhlásky (h ch k r d t n), " +
                "které jsou v domácích slovech následovány y/ý. (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.I_PO_MEKKYCH_OBOJETNYCH_SOUHLASKACH)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void zajmenaMneMe() { // Mně - Mě

        mistake = new Mistake(2, "ně", "ě", "Mně", "Mě", "", "se", 1,
                "mně", "k3", "M(ně)<ě> se líbí Ivana.");


        String description = "Tvar mě se používá ve 2. a 4. pádu (Na mě nezbylo nic). Tvar mně ve 3. a 6. pádu. " +
                "(Mně se líbí Ivana. -> Komu, čemu?, 3. pád.) Pomůcka: " +
                "Zaměnit mě/mně za tebe/tobě. Tebe nahrazuje mě (Na tebe nezbylo nic.), tobě nahrazuje mně " +
                "(Tobě se líbí Ivana.). (VOJ)";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.ZAJMENA_A_SLOVA_OBSAHUJICI_MNE_ME)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }

    @Test
    public void slovaObsahujiciMeMne() { // Zapomněl - Zapoměl

        mistake = new Mistake(6, "ně", "ě", "Zapomněl", "Zapoměl", "", "jsem", 1,
                "zapomněl", "k5", "Zapom(ně)<ě>l jsem rozsvítit, tak jsem padl do kůlny.");


        String description = "Souhláska n po souhlásce m označuje výslovnost [ň + e]; např. měď, měkký. " +
                "Tam, kde je souhláska [ň] nebo [n] i v jiném tvaru slova nebo v slově příbuzném, píše se mně; " +
                "např. zapomněl (pomni), rozumně (rozumný).";

        Mistake mistakeResultNato = cr.applyRules(mistake);

        assertThat(mistakeResultNato.getMistakeType(), is(equalTo(MistakeType.ZAJMENA_A_SLOVA_OBSAHUJICI_MNE_ME)));
        assertThat(mistakeResultNato.getPriority(), is(equalTo(5)));
        assertThat(mistakeResultNato.getMistakeDescription(), is(equalTo(description)));
    }
}