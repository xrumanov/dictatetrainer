package cz.muni.fi.dictatetrainer.corrector.rules.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.model.MistakeType;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of non-contextual corrector rules
 * Only shows description for mistakes that are non-contextual (without a context of by-standing words)
 * <p> TODO how to choose priority number
 *  TODO consider moving definitions somewhere else to not have that much data in mistake
 */
public class CorrectorRulesNoContext implements CorrectorRules {

    public CorrectorRulesNoContext() {

    }

    @Override
    public Mistake applyRules(Mistake mistake) {

        String mostGeneralRule = "Pro tuto chybu ještě nemáme vysvetlení";
        String description;

        char[] precedingCharacters = new char[]{'b', 'l', 'm', 'p', 's', 'v', 'z'};

        for (char precedingCharacter : precedingCharacters) {

            if ((description = vyjmenovaneSlovoDubletOrNull(mistake, precedingCharacter)) != null ||
                    (description = vyjmenovaneSlovoOrNull(mistake, precedingCharacter)) != null) {
                mistake.setMistakeType(MistakeType.VYJMENOVANA_SLOVA);
                mistake.setMistakeDescription(description);
                mistake.setPriority(10);
                return mistake;
            }
        }

        if ((description = iyPoPismenuCOrNull(mistake)) != null) {
            mistake.setMistakeType(MistakeType.IY_po_C);
            mistake.setMistakeDescription(description);
            mistake.setPriority(10);
            return mistake;

        } else if ((description = predponySZOrNull(mistake)) != null) {
            mistake.setMistakeType(MistakeType.PREDPONY_S_Z);
            mistake.setMistakeDescription(description);
            mistake.setPriority(7);
            return mistake;

        } else if ((description = predlozkySZOrNull(mistake)) != null) {
            mistake.setMistakeType(MistakeType.PREDLOZKY_S_Z);
            mistake.setMistakeDescription(description);
            mistake.setPriority(7);
            return mistake;

        } else if ((description = prejataSlovaSZOrNull(mistake)) != null) {
            mistake.setMistakeType(MistakeType.PREJATA_SLOVA_S_Z);
            mistake.setMistakeDescription(description);
            mistake.setPriority(7);
            return mistake;

        } else if ((description = psaniDisDysOrNull(mistake)) != null) {
            mistake.setMistakeType(MistakeType.DIS_DYS);
            mistake.setMistakeDescription(description);
            mistake.setPriority(8);
            return mistake;

        } else if ((description = psaniSeZakoncenimManieOrNull(mistake)) != null) {
            mistake.setMistakeType(MistakeType.SLOVA_ZAKONCENE_MANIE);
            mistake.setMistakeDescription(description);
            mistake.setPriority(6);
            return mistake;

        } else if ((description = psaniNnNOrNull(mistake)) != null) {
            mistake.setMistakeType(MistakeType.PSANI_N_NN);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else {
            mistake.setMistakeType(MistakeType.OSTATNI);
            mistake.setMistakeDescription(mostGeneralRule);
            mistake.setPriority(0);
            return mistake;
        }
    }

    //-------------Pravopis----------------

    private String vyjmenovaneSlovoDubletOrNull(Mistake mistake, char precededChar) {

        String tag = mistake.getPosTag();
        String lemma = mistake.getLemma();
        String lemmaWithoutAccentsOnIY = lemma.replaceAll("ý", "ý").replaceAll("í", "i");
        String writtenWord = mistake.getWrittenWord();
        Integer mistakeCharPosition = mistake.getMistakeCharPosInWord();
        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        Map<String, String> vyjmenovanaSlovaDublet;

        switch (precededChar) {
            case 'b':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoBDublet();
                break;
            case 'l':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoLDublet();
                break;
            case 'm':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoMDublet();
                break;
            case 'p':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoPDublet();
                break;
            case 's':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoSDublet();
                break;
            case 'v':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoVDublet();
                break;
            case 'z':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoZDublet();
                break;
            default:
                return null; //invalid character - not supported
        }

        Set<String> keySet = vyjmenovanaSlovaDublet.keySet();

        if (((correctChars.equals("y") && writtenChars.equals("i"))
                || (correctChars.equals("ý") && writtenChars.equals("í"))
                || (correctChars.equals("i") && writtenChars.equals("y"))
                || (correctChars.equals("í") && writtenChars.equals("ý")))
                && (((!tag.startsWith("k2")) && vyjmenovanaSlovaDublet.containsKey(lemma))
                || (tag.startsWith("k2") && (prefixMatchesSet(lemmaWithoutAccentsOnIY, keySet) != null)
                && mistakeCharPosition != mistake.getCorrectWord().length()))
                && writtenWord.charAt(mistakeCharPosition - 1) == (lemma.charAt(mistakeCharPosition - 1))
                && writtenWord.charAt(mistakeCharPosition - 2) == (lemma.charAt(mistakeCharPosition - 2))
                && lemma.charAt(mistakeCharPosition - 2) == precededChar) {

            if (precededChar == 'b') {
                String definition = "Ke slovesům být (= existovat) i bít (= tlouct) tvoříme řadu předponových sloves " +
                        "(popř. přídavných jmen). Rozlišujeme: ";
                return definition + " " + vyjmenovanaSlovaDublet.get(lemma);
            } else {
                return vyjmenovanaSlovaDublet.get(lemma);
            }
        }
        return null;
    }

    private String vyjmenovaneSlovoOrNull(Mistake mistake, char precededChar) {

        String lemma = mistake.getLemma();
        String tag = mistake.getPosTag();
        Integer mistakeCharPosition = mistake.getMistakeCharPosInWord();
        String writtenWord = mistake.getWrittenWord();
        char correctChars = mistake.getCorrectChars().toCharArray()[0];
        char writtenChars = mistake.getWrittenChars().toCharArray()[0];
        Map<String, String> vyjmenovanaSlova;

        String generalDefinition = "Jde o vyjmenované nebo z cizího jazyka přejaté slovo. " +
                "Tato slova mají společné psaní i/í po souhláskách (b, f, m, l, p, s, v, z), " +
                "které jsou tzv. obojetné, a ke každému ze zmíněných písmen existuje seznam výjimek, " +
                "u nichž se na místo i/í píše y/ý jako v tomto případě. Důvod je historický a u každého slova jiný, " +
                "je nutné si jej pamatovat. (VOJ)";

        switch (precededChar) {
            case 'b':
                vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoB();
                break;
            case 'l':
                vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoL();
                break;
            case 'm':
                vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoM();
                break;
            case 'p':
                vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoP();
                break;
            case 's':
                vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoS();
                break;
            case 'v':
                vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoV();
                break;
            case 'z':
                vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoZ();
                break;
            default:
                return null; //invalid character - not supported
        }

        if ((((correctChars == 'y' && writtenChars == 'i'))
                || (correctChars == 'ý' && writtenChars == 'í'))
                && lemma.charAt(mistakeCharPosition - 2) == precededChar && vyjmenovanaSlova.containsKey(lemma)
                && ((writtenWord.charAt(mistakeCharPosition - 1) == writtenChars) && (lemma.charAt(mistakeCharPosition - 1)) == correctChars)
                && (writtenWord.charAt(mistakeCharPosition - 2) == (lemma.charAt(mistakeCharPosition - 2)))
                && (!tag.startsWith("k2") || tag.startsWith("k2") && mistakeCharPosition != mistake.getCorrectWord().length())) {
            if (lemma.startsWith("fyto")) {
                String definition = "Slova s předponou fyto- patří mezi vyjmenovaná slova po f.";
                return vyjmenovanaSlova.get(lemma) + " " + definition + generalDefinition;
            } else if (lemma.startsWith("vý") || lemma.startsWith("vy")) {
                String definition = "Slova s předponou vy-/vý- patří mezi vyjmenovaná slova po v.";
                return vyjmenovanaSlova.get(lemma) + " " + definition + " " + generalDefinition;
            }
            return vyjmenovanaSlova.get(lemma) + " " + generalDefinition;
        }
        return null;
    }

    private String iyPoPismenuCOrNull(Mistake mistake) {

        String lemma = mistake.getLemma();
        String tag = mistake.getPosTag();
        Integer mistakeCharPosition = mistake.getMistakeCharPosInWord();
        String writtenWord = mistake.getWrittenWord();
        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();


        if (((correctChars.equals("y") && writtenChars.equals("i"))
                || (correctChars.equals("ý") && writtenChars.equals("í"))
                || (correctChars.equals("i") && writtenChars.equals("y"))
                || (correctChars.equals("í") && writtenChars.equals("ý")))
                && lemma.charAt(mistakeCharPosition - 2) == 'c'
                && writtenWord.charAt(mistakeCharPosition - 1) == (lemma.charAt(mistakeCharPosition - 1))
                && writtenWord.charAt(mistakeCharPosition - 2) == (lemma.charAt(mistakeCharPosition - 2))
                && (!tag.startsWith("k2") || tag.startsWith("k2") && mistakeCharPosition != mistake.getCorrectWord().length())) {

            return "Po c se v domácích slovech píše měkké i (např. cit, církev atd.). " +
                    "U cizích slov pravidlo o tom, že se po c píše měkké i, neplatí. Na jedné straně máme " +
                    "slova jako citron, nacista atp., na straně druhé máme slova, v nichž se po c píše tvrdé y – " +
                    "např. cyklon, cynik apod. Pro psaní i a y v těchto slovech neexistuje žádné pravidlo, musíme si " +
                    "pamatovat jednotlivé příklady. (IJP)";
        }
        return null;
    }

    private String predponySZOrNull(Mistake mistake) {

        Integer mistakeCharPosition = mistake.getMistakeCharPosInWord();
        List<String> predponySZ = CorrectorRulesStaticLists.getPredponySZ();
        Map<String, String> predponySZdublet = CorrectorRulesStaticLists.getPredponySZDublet();
        String lemma = mistake.getLemma();
        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();

        if (mistakeCharPosition == 1 && ((correctChars.equals("s") && writtenChars.equals("z"))
                || (correctChars.equals("z") && writtenChars.equals("s")))) {
            if (predponySZ.contains(lemma)) {
                return "NECHYBA U skupiny sloves mezi podobami s předponou s- a z- zásadní " +
                        "významový rozdíl není, a můžete je psát oběma způsoby: " +
                        "zcestovat i scestovat, zkrápět i skrápět... (IJP)";

            } else if (predponySZdublet.containsKey(lemma)) {
                String definition = "Některá slova lze psát jak s předponou s-, tak s předponou z-. " +
                        "U většiny z nich je mezi oběma podobami zřetelný významový rozdíl. (IJP)";

                return definition + " " + predponySZdublet.get(lemma);
            } else if (writtenChars.equals("s") && lemma.endsWith("ovat")) {
                return "U sloves zakončených na -ovat (aktualizovat, kopírovat) je podoba " +
                        "s předponou z- vždy správná. Jen u některých z nich lze zvolit i předponu s- " +
                        "(zestylizovat i sestylizovat, zkontaktovat i skontaktovat). " +
                        "Výjimkou jsou pouze ta slovesa, která mají s- už v původním jazyku: " +
                        "skandalizovat (neexistuje samotné kandalizovat), skandovat.	(IJP)";

            } else {
                return "Předpona s(e)- naznačuje " +
                        "a) směřování dohromady: scelit, shromáždit se, spojit; " +
                        "b) směřování shora dolů, po povrchu pryč: sklonit, sklopit, shýbnout se; " +
                        "c) zmenšení objemu, až zánik (scvrknout se, shořet). " +
                        "U mnohých slov je třeba si způsob psaní pamatovat, neboť původní význam si " +
                        "dnes již neuvědomujeme: skončit, slevit (sleva)." +
                        "Předponou z(e)- se tvoří: " +
                        "a) od nedokonavých sloves dokonavá, která nemají žádný z významů (moknout › zmoknout); " +
                        "b) od podstatných nebo přídavných jmen slovesa mající význam " +
                        "‚učinit nebo stát se tím, co znamená slovo základové‘: ocel › zocelit. " +
                        "U mnohých slov si je třeba způsob psaní pamatovat: zkoumat, zkoušet (IJP)";
            }
        }
        return null;
    }

    private String predlozkySZOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        int correctWordLength = mistake.getCorrectWord().length();

        if (((correctChars.equals("s") && writtenChars.equals("z"))
                || (correctChars.equals("z") && writtenChars.equals("s")))
                && ((correctWordLength == 1) || (correctWordLength == 2 && writtenChars.charAt(1) == 'e'))) {
            return "S druhým pádem se pojí předložka z (ze). Např. vstal z postele; vyšel z banky. " +
                    "Se sedmým pádem se pojí jedině předložka s (se). " +
                    "Např. trávil čas s knihou; s pilinami si lze hrát. (IJP)";

        }
        return null;
    }

    private String prejataSlovaSZOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        Integer mistakeCharPosition = mistake.getMistakeCharPosInWord();
        String lemma = mistake.getLemma();

        List<String> prejataSlovaSZ = CorrectorRulesStaticLists.getPrejataSlovaSZ();
        Map<String, String> prejateSlovaSZDublet = CorrectorRulesStaticLists.getPredponySZDublet();
        List<String> prejataSlovaSZizmus = CorrectorRulesStaticLists.getPrejataSlovaSZIzmusZmus();

        if (((correctChars.equals("s") && writtenChars.equals("z"))
                || (correctChars.equals("z") && writtenChars.equals("s")))
                && mistakeCharPosition != 1) {
            if (prejataSlovaSZ.contains(lemma)) {
                return "NECHYBA Ve slovech zakončených v 1. pádě " +
                        "na skupinu vyslovovanou [-ns, -rs, -ls, např. kurs], v ostatních pádech " +
                        "a ve slovech odvozených vyslovovanou [-nz-, -rz-, -lz-, např. bez kurzu], " +
                        "jsou obě možnosti psaní rovnocenné, a to bez stylového rozlišení. (IJP)";
                // TODO AND další znakovou neshodu v chybném lemmatu, která se týká kvantity vokálů, ignorovat

            } else if (prejateSlovaSZDublet.containsKey(lemma)) {
                return "Ve slovech zdomácnělých, kde se původní s vždy v češtině vyslovuje jako [z], " +
                        "se podoby se z považují za základní, tedy stylově neutrální. " +
                        "Podoby se s jsou stylově příznakové, proto se užívají ve specifických " +
                        "(např. úzce odborných) textech. Např.: analýza – analysa, azyl – asyl";
                // TODO AND další znakovou neshodu v chybném lemmatu, která se týká kvantity vokálů, ignorovat

            } else if (prejataSlovaSZizmus.contains(lemma)) {
                return "Ve slovech s příponou vyslovovanou jako [-izmus]/[-zmus] a [-zmus]/[-zma] " +
                        "můžeme psát s i z, přičemž podoba se s se považuje za základní. (IJP)";
                // TODO AND další znakovou neshodu v chybném lemmatu, která se týká kvantity vokálů, ignorovat
            }
        }
        return null;
    }

    private String psaniDisDysOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        String lemma = mistake.getLemma();

// TODO chyba je přítomna i v lemmatickém tvaru AND lemma chybného slova je přítomno v seznamu

        Map<String, String> psaniDisDys = CorrectorRulesStaticLists.getDisDys();
        if (((correctChars.equals("y") && writtenChars.equals("i"))
                || (correctChars.equals("i") && writtenChars.equals("y"))
                && (correctWord.startsWith("dis") || correctWord.startsWith("dys"))
                && psaniDisDys.containsKey(lemma))) {
            String definition = "Významově poměrně blízké předpony dis- a dys- mají rozdílný původ. " +
                    "O jejich užití obvykle rozhoduje tradice. Latinské dis- odpovídá české předponě roz-, " +
                    "popř. ne-. Řecké dys- znamená ‚zeslabený, vadný, porušený‘, " +
                    "předpona vyjadřuje něco negativního, špatného. (IJP)";

            return definition + " " + psaniDisDys.get(lemma);
        }
        return null;
    }

    private String psaniSeZakoncenimManieOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String lemma = mistake.getLemma();

        if ((correctChars.equals("a") && writtenChars.equals("á"))
                || (correctChars.equals("á") && writtenChars.equals("a"))) {

            if ((lemma.endsWith("manie") || lemma.endsWith("mánie")) && lemma.length() > 5) {

                String definition = "NECHYBA Ve složeninách obsahujících -manie je možné psát -mánie i -manie. (VOJ)";

                return definition;

            } else if (lemma.equals("mánie")) {
                return "Přípustná je pouze varianta mánie. " +
                        "Pouze pokud jde o slovo složené (např. cestománie, pokemanie) " +
                        "je možné psát krátce i dlouze. (VOJ)";

            }
        }
        return null;
    }

    private String psaniNnNOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();

        if ((correctChars.equals("n") && writtenChars.equals(""))
                || (correctChars.equals("") && writtenChars.equals("n"))
                && (mistakeCharPosInWord != 1) &&
                ((correctWord.charAt(mistakeCharPosInWord) == 'n') || (correctWord.charAt(mistakeCharPosInWord - 2)) == 'n')) {

            return "Dvě n se píšou u přídavných jmen odvozených příponou -ní, -ný od podstatných jmen, " +
                    "jejichž kořen končí na -n,, -ň (den – denní, holeň – holenní). Dvě n se píšou " +
                    "i u přídavných jmen (a u výrazů od nich odvozených), u nichž výchozí podstatné jméno neexistuje " +
                    "nebo se neužívá, ale kořen je zakončen na -n (bezcenný (bezcennost)). Píše se také v druhých a " +
                    "třetích stupních přídavných jmen a slovech od nich odvozených." +
                    "Jedno n se píše v případech: a) u přídavných jmen odvozených příponou -í od jmen pojmenovávajících " +
                    "zvířata, jejichž kořen končí na -n: (havran – havraní), a od příčestí trpných: dán – daný; " +
                    "b) slovo raný ve významu ,brzký, časný‘ (raná gotika) a od r. 1957 též dceřiný; " +
                    "c) u přídavných jmen utvořených od podstatných jmen příponou -ěný: vlna – vlněný (stejně jako sláma – slaměný);" +
                    "d) podstatná jména odvozená od přídavných jmen na -ní, -ný příponami -ík, -ice, -ina: deník, okenice, cenina. (IJP)";

        }
        return null;
    }

    private String psaniSprezekSprahovaniOrNull(Mistake mistake) {

        String correctWord = mistake.getCorrectWord();
        String writtenWord = mistake.getWrittenWord();

        if ((writtenWord.equals("zato") && (correctWord.equals("za to"))) ||
                (writtenWord.equals("za to") && (correctWord.equals("zato")))) {

            return "Příslovce zato užíváme ve významu ‚náhradou za něco, místo něčeho‘ " +
                    "(Vrátil se s nepořízenou, zato rozčílený.). Zato také funguje ve významu spojky odporovací, " +
                    "synonymickým výrazem je však: Učit se nechtěl, zato se uměl prát. " +
                    "V ostatních případech píšeme výraz za to zvlášť: Mám za to, že je schůze už zbytečně dlouhá. (IJP)";

        } else if ((writtenWord.equals("nato") && (correctWord.equals("na to"))) ||
                (writtenWord.equals("na to") && (correctWord.equals("nato")))) {

            return "Příslovce nato znamená ‚potom, poté‘: Vypil láhev vitriolu, brzy nato zemřel. " +
                    "Hned nato začala předstírat, že se nic neděje. " +
                    "Zvlášť píšeme spojení předložky a zájmena: Jdeme na to. (IJP)";

        }  else if ((writtenWord.equals("přitom") && (correctWord.equals("pŕi tom")))
                || (writtenWord.equals("pŕi tom") && (correctWord.equals("přitom")))
                || (writtenWord.equals("potom") && (correctWord.equals("po tom")))
                || (writtenWord.equals("po tom") && (correctWord.equals("potom")))) {

            return "Zde není vymezení tak jednoznačné. Oslabuje se totiž chápání tohoto výrazu jako spojení předložky " +
                    "a ukazovacího zájmena a ve většině textů lze volbu odůvodnit jak významem ‚zároveň‘ (přitom), " +
                    "tak vztáhnutím na děj předchozí věty, ke kterému zájmeno odkazuje (při tom)." +
                    "Podobný případ je předložka + zájmeno po tom a příslovce potom. " +
                    "Hrál a potom zpíval (‚následovně‘). Hrál a po tom zpíval. " +
                    "(= Po hraní na hudební nástroj začal zpívat.) (IJP)";
        }
        return null;
    }

    private String slozenaPridavniJmenaOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();

        if ((correctChars.equals("-") && writtenChars.equals(""))
                && (mistakeCharPosInWord != 1) && (mistakeCharPosInWord != correctWord.length())
                && (correctWord.contains("sko-") || correctWord.contains("cko-")
                || correctWord.contains("ně-") || correctWord.contains("ově-"))) {

            return "V těchto případech používáme pro oddělení obou složek spojovník " +
                    "(ten zde v podstatě plní funkci spojky a). " +
                    "Např.: zemědělsko-potravinářský (týkající se zemědělství a potravinářství). " +
                    "Někdy je užíván k rozlišení: politicko-ekonomický (souřadicí; týkající se politiky a ekonomie) " +
                    "a politickoekonomický (podřadicí; týkající se politické ekonomie) (IJP)";
        }
        return null;
    }

    private String velkaPismenaOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String writtenWord = mistake.getWrittenWord();

        String sentence = mistake.getSentence();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();

        if ((correctChars.length() == 1 && writtenChars.length() == 1)
                && (Character.isLowerCase(correctChars.toCharArray()[0])
                && Character.isUpperCase(writtenChars.toCharArray()[0])
                || (Character.isUpperCase(correctChars.toCharArray()[0]))
                && (Character.isLowerCase(writtenChars.toCharArray()[0])))
                && writtenChars.toLowerCase().equals(correctChars.toLowerCase())) {

            if (sentence.split("\\s+")[0].equals(writtenWord)) {
                String definition = "Věta vždy začíná velkým písmenem.";

                return definition;

            } else if (mistakeCharPosInWord == 1) {
                return "Názvy jednoslovné (např. jméno Miloš, město Kladno) začínají velkým písmenem. " +
                        "U víceslovných názvů (např. Mrtvé moře) se píše jako velké pouze první písmeno, pokud už " +
                        "neobsahují další název (např. Ústí nad Labem, řeka Labe). K těmto základním pravidlům však " +
                        "existuje množství dodatků (k nalezení např. v Pravidlech českého pravopisu), " +
                        "např. o psaní názvů ulic, živých bytostí, institucí atd. (VOJ)";

            }
        }
        return null;
    }

    //------------Slovotvorba-------------------

    private String adjZakoncenaIciOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        String lemma = mistake.getLemma();

        if ((correctChars.equals("i") && writtenChars.equals("í"))
                || (correctChars.equals("í") && writtenChars.equals("i"))
                && (lemma.endsWith("icí") || lemma.endsWith("ící"))
                && mistakeCharPosInWord == correctWord.length() - 2) {

            return "Podoby typu měřicí, řídicí jsou přídavná jména účelová. Vyjadřují, k čemu věc slouží: " +
                    "měřicí zařízení je zařízení určené k měření, řídicí (ovládací) panel je pojmenován podle toho, " +
                    "že slouží k řízení (ovládání). Přídavná jména typu měřící, řídící se nazývají dějová. " +
                    "Jimi se vyjadřuje děj (činnost) právě probíhající: např. řídící žena je žena, která právě řídí. " +
                    "U osob se podoby na -ící používají výhradně: řídící pracovník, velící důstojník, místodržící." +
                    "Pomůcka: Do daného spojení dosadit jeden člen z dvojic typu: vzdělávací – vzdělávající. " +
                    "Je-li vzdělávací středisko, mělo by být i školicí středisko, je-li útvar provádějící činnost, " +
                    "pak je i útvar řídící činnost. (IJP)";
        }
        return null;
    }

    private String adjZakoncenaNiOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        String lemma = mistake.getLemma();
        Map<String, String> adjektivaZakoncenaNiNyList = CorrectorRulesStaticLists.getAdjektivaZakoncenaNiNy();
        Map<String, String> adjektivaZakoncenaNiNyVyznamoveTotozneList
                = CorrectorRulesStaticLists.getAdjektivaZakoncenaNiNyVyznamoveTotozne();
        Map<String, String> adjektivaZakoncenaNiNyJineZakladyList
                = CorrectorRulesStaticLists.getAdjektivaZakoncenaNiNyJineZaklady();

        if ((correctChars.equals("í") && writtenChars.equals("ý"))
                || (correctChars.equals("ý") && writtenChars.equals("í"))
                && mistakeCharPosInWord == correctWord.length()
                && (correctWord.charAt(mistakeCharPosInWord - 1) == 'n')) {

            if (adjektivaZakoncenaNiNyList.containsKey(lemma)) {
                String definition = "Nevelká skupina jmen, u nichž si -ní a -ný buď konkurují, nebo slouží k rozlišení " +
                        "různých významů. Zakončení na -ný mají přídavná jména s významem hodnotícím, proto " +
                        "např. spojením zábavný pořad vyjadřujeme svůj názor na charakter pořadu, hodnotíme jej, " +
                        "kdežto jako zábavní pořad může být tvůrci označen i pořad, který diváci po zhlédnutí možná " +
                        "ani za zábavný považovat nebudou. (IJP)";
                return definition + " " + adjektivaZakoncenaNiNyList.get(lemma);

            } else if (adjektivaZakoncenaNiNyVyznamoveTotozneList.containsKey(lemma)) {
                String definition = "NECHYBA Významově totožné varianty.";
                return definition + " " + adjektivaZakoncenaNiNyVyznamoveTotozneList.get(lemma);

            } else if (adjektivaZakoncenaNiNyJineZakladyList.containsKey(lemma)) {
                String definition = "Tato jména jsou tvořena od různých základů slov.";

                return definition + adjektivaZakoncenaNiNyJineZakladyList.get(lemma);
            }
        }
        return null;
    }

    private String typAckoliAckolivOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        String tag = mistake.getPosTag();

        if ((correctChars.equals("v") && writtenChars.equals(""))
                || (correctChars.equals("") && writtenChars.equals("v"))
                && (correctWord.length() == mistakeCharPosInWord)
                && (correctWord.endsWith("koli")) || (correctWord.endsWith("koliv"))
                && tag.startsWith("k1")) {

            return "NECHYBA Zájmena jako např. kdokoli(v), cokoli(v) atd., příslovce kdykoli(v), " +
                    "kamkoli(v), částice nikoli(v) a spojka ačkoli(v) " +
                    "mohou mít dvě zcela rovnocenné podoby zakončení: -koli a -koliv. (IJP)";

        }
        return null;
    }

    private String vokalizacePredlozekOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        String nextWord = mistake.getNextWord();
        char precedingChar = writtenChars.charAt(mistakeCharPosInWord - 2);
        List<Character> vokaly = CorrectorRulesStaticLists.getVOKALY();
        List<Character> konsonanty = CorrectorRulesStaticLists.getKONSONANTY();


        if ((writtenChars.equals("") && correctChars.equals("e"))) {

            if ((correctWord.length() == 2)
                    && (precedingChar == 'k' || precedingChar == 's' || precedingChar == 'v' || precedingChar == 'z')) {
                if (precedingChar == nextWord.charAt(0)) {
                    return "Před slovem, které začíná stejnou souhláskou jako předložka, " +
                            "vokalizujeme vždy. Např.: ke kořenům, se sestrou. (IJP)";
                } else if (((precedingChar == 's')
                        && ((nextWord.charAt(0) == 'z') || (nextWord.charAt(0) == 'ž') || (nextWord.charAt(0) == 'š')))
                        || ((precedingChar == 'z')
                        && ((nextWord.charAt(0) == 's') || (nextWord.charAt(0) == 'š') || (nextWord.charAt(0) == 'ž')))
                        || ((precedingChar == 'v')
                        && ((nextWord.charAt(0) == 'f')))
                        || ((precedingChar == 'k')
                        && ((nextWord.charAt(0) == 'g')))) {

                    return "Tendence k vokalizaci je silná, pokud předložka stojí před slovem začínajícím podobnou " +
                            "souhláskou, tedy s před z-, ž-, š-; z před s-, š-, ž-; v před f-; k před g-. " +
                            "Např.: ke groši, se zemí, ve Finsku. U některých slov však pravidlo nemusí " +
                            "platit (Např.: nevycházím z šoku). Důležitou roli zde hraje právě faktor výslovnosti. (IJP)";
                }
            }
        } else if ((writtenChars.equals("e") && correctChars.equals(""))) {
            if ((correctWord.length() == 1)
                    && (precedingChar == 'k' || precedingChar == 's' || precedingChar == 'v' || precedingChar == 'z')) {
                if (vokaly.contains(nextWord.charAt(0))) {
                    return "Před slovem začínajícím samohláskou se neslabičné předložky nevokalizují " +
                            "(z \"k\" se nestane \"ke\" atp.). Např.: k ústavě, s ementálem. (IJP)";

                } else if ((precedingChar == 's') && (nextWord.equals("sebou"))) {
                    return "Výjimka, jde o ustálené spojení. Vyskytuje se pouze v instrumentálu. " +
                            "Např. brát s sebou, vzít s sebou. Sebou vyjadřuje pohyb těla (mrskat sebou, …). (IJP)";
                } else if ((konsonanty.contains(nextWord.charAt(0))) && (vokaly.contains(nextWord.charAt(1)))
                        || (nextWord.startsWith("ch") && (vokaly.contains(nextWord.charAt(2))))) {
                    return "Před jednou souhláskou se neslabičné předložky nevokalizují. Např.: k řece, s pentličkami. (IJP)";
                }
            }
        } else if ((writtenChars.equals("u") && correctChars.equals(""))
                && (precedingChar == 'k')
                && ((correctWord.length() == 1) || (correctWord.length() == 2))) {
            return "Ku se dochovalo pouze v některých ustálených spojeních, " +
                    "a to jen před slovy začínajícími retnou souhláskou, zejména p " +
                    "(např. ku příkladu, ku prospěchu) a ve vyjadřování poměrnosti (např. pět ku sedmi). (IJP)";
        }
        return null;
    }

    private String zajmenaVasiJiNiOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();

        if (((writtenChars.equals("i") && correctChars.equals("í"))
                || (writtenChars.equals("í") && correctChars.equals("i"))) &&
                ((correctWord.length() == 2 && ((correctWord.charAt(0) == 'j') || correctWord.charAt(0) == 'n'))
                        || ((correctWord.length()) == 4 && ((correctWord.startsWith("vaš")) || (correctWord.startsWith("naš")))))) {

            return "Krátké varianty ji/ni, vaši/naši apod. jsou vázány pouze ke 4. pádu, " +
                    "ostatní pády jsou jí/ní vaší/naší. Pomůcka: Za dané zájmeno dosadit " +
                    "ukazovací zájmena té, tou a tu. Pokud se do věty hodí zájmeno tu, patří sem ni, ji " +
                    "(Nestojím o ni. -> Nestojím o tu dívku.). " +
                    "V ostatních případech (té, tou) sem patří jí, ní (Šli bez ní. -> Šli bez té dívky.). (VOJ)";
        }
        return null;
    }

    private String psaniBeBjeVeVjePeOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        char precedingChar = writtenChars.charAt(mistakeCharPosInWord - 2);

        if ((writtenChars.equals("ě") && correctChars.equals("je"))
                || (writtenChars.equals("je") && correctChars.equals("ě"))
                && ((precedingChar == 'b') || (precedingChar == 'v') || (precedingChar == 'p'))) {

            return "Skupiny bje a vje píšeme u těch slov, která jsou tvořena předponami ob- a v-, " +
                    "přičemž kořen slova začíná na je-. (ob-jezdit – objezd; v-jet – vjezd). " +
                    "U ostatních slov píšeme ě (oběd, pěnkava, větev). V žádném českém slovu se nevyskytuje pjě. " +
                    "Řídkou výjimkou jsou některé názvy, např. Skopje. (VOJ)";

        }
        return null;
    }

    private String souhlaskyParoveOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        char nextChar = writtenChars.charAt(mistakeCharPosInWord);

        if ((writtenChars.equals("b") && correctChars.equals("p"))
                || (writtenChars.equals("p") && correctChars.equals("b"))
                || (writtenChars.equals("d") && correctChars.equals("t"))
                || (writtenChars.equals("t") && correctChars.equals("d"))
                || (writtenChars.equals("ď") && correctChars.equals("ť"))
                || (writtenChars.equals("ť") && correctChars.equals("ď"))
                || (writtenChars.equals("g") && correctChars.equals("k"))
                || (writtenChars.equals("k") && correctChars.equals("g"))
                || (writtenChars.equals("v") && correctChars.equals("f"))
                || (writtenChars.equals("f") && correctChars.equals("v"))
                || (writtenChars.equals("ž") && correctChars.equals("š"))
                || (writtenChars.equals("š") && correctChars.equals("ž"))
                || (writtenChars.equals("c") && correctChars.equals(""))
                || (writtenChars.equals("") && correctChars.equals("c"))
                ) {

            if ((mistakeCharPosInWord == correctWord.length()) || ((writtenChars.equals("c") && correctChars.equals(""))
                    && (mistakeCharPosInWord == (correctWord.length() - 1)) && nextChar == 'h')) {

                return "Souhlásky rozdělujeme na znělé (b, d, ď, g, h, v, z, ž) a neznělé (p, t, ť, k, ch, f, s, š). " +
                        "Na konci slov je každá souhláska vyslovována nezněle. Pomůcka: Říci si slovo v jiném tvaru. " +
                        "Např. potah zní jako [potach], jiný tvar: bez potahu. (VOJ)";

            } else if (mistakeCharPosInWord != correctWord.length()) {

                return "Skupina párových souhlásek se vyslovuje celá buď zněle, nebo nezněle, " +
                        "ale označování jednotlivých hlásek se řídí podle toho, které hlásky jsou " +
                        "v jiných tvarech téhož slova nebo ve slovech příbuzných, " +
                        "např. vézt (vezu), kavka (kavek), sjezd (sjezdu), lehký (lehounký). (PČP)";
            }
        }
        return null;
    }

    private String diakritikaOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        String lemma = mistake.getLemma();
        Set<String> predpony = CorrectorRulesStaticLists.getPredponySlovesneAOstatni();
        String writtenWord = mistake.getWrittenWord();
        char nextChar = writtenChars.charAt(mistakeCharPosInWord);


        if ((writtenChars.equals("u") && correctChars.equals("ú"))
                || (writtenChars.equals("ů") && correctChars.equals("ú"))) {

            if (mistakeCharPosInWord == 1) {
                return "Na začátku slova se dlouhé u vždy vyjadřuje jako ú. (VOJ)";

            } else if ((mistakeCharPosInWord > 1) && ((lemma.endsWith("úhelník"))
                    || (((prefixMatchesSet(lemma, predpony)) != null)
                    && (writtenWord.charAt((prefixMatchesSet(lemma, predpony)).length() + 1) == 'ú')))) {

                return "Slovo složené z více kořenů (např. troj + úhelník) si ú uchovává i uvnitř slova. (VOJ)";
            } else {

                return "Jde o přejaté slovo (ocún, múza), které se takto odlišuje od slov domácích (kůlna, důl, …). " +
                        "Ú se vyskytuje také u citoslovcí (hú, vrkú). (VOJ)";
            }
        } else if ((writtenChars.equals("u") && correctChars.equals("ů"))
                || (writtenChars.equals("ú") && correctChars.equals("ů"))) {

            return "U nepřejatých slov (např. kůlna, důl) je dlouhé u, které není na začátku slova, vyjádřeno jako ů.";
        } else if (((writtenChars.equals("ď") && correctChars.equals("d"))
                || (writtenChars.equals("ť") && correctChars.equals("t"))
                || (writtenChars.equals("ň") && correctChars.equals("n")))
                && ((nextChar == 'i') || (nextChar == 'í') || (nextChar == 'ě'))) {

            return "Háček (tzv. klička) zaniká, i přes odlišnou výslovnost. " +
                    "Měkkost přechází na samohlásku e (stane se z ní ě, např.: dělat) nebo i (např.: dílna). (VOJ)";

        } else if ((writtenChars.equals("d") && correctChars.equals("ď"))
                || (writtenChars.equals("t") && correctChars.equals("ť"))
                || (writtenChars.equals("n") && correctChars.equals("ň"))) {

            return "U ď, ť, ň na konci slova dochází k uchování háčku (např.: loď, choť). " +
                    "Stejně tak, pokud následují samohlásky a, o, u. (např.: ďábel) (VOJ)";
        }
        return null;

    }

    private String iPoMekkychaObojetnychSouhlaskachOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        char precedingChar = writtenChars.charAt(mistakeCharPosInWord - 2);
        List<Character> souhlaskyMekkeObojetne = CorrectorRulesStaticLists.getSouhlaskyMekkeAObojetne();
        List<Character> souhlaskyMekke = CorrectorRulesStaticLists.getSouhlaskyMekke();
        List<Character> souhlaskyObojetne = CorrectorRulesStaticLists.getSouhlaskyObojetne();
        List<Character> souhlaskyTvrde = CorrectorRulesStaticLists.getSouhlaskyTvrde();
        String lemma = mistake.getLemma();
        String tag = mistake.getPosTag();

        if ((writtenChars.equals("ý") && correctChars.equals("í"))
                || (writtenChars.equals("y") && correctChars.equals("i"))
                && (souhlaskyMekkeObojetne.contains(precedingChar))
                && (lemma.charAt(mistakeCharPosInWord - 2) == precedingChar)
                && (lemma.charAt(mistakeCharPosInWord - 1) == 'i') || (lemma.charAt(mistakeCharPosInWord - 1) == 'í')
                && ((!tag.startsWith("k2")) || (tag.startsWith("k2") && mistakeCharPosInWord != correctWord.length()))) {

            if (souhlaskyObojetne.contains(precedingChar)) {
                return "Tato slova mají společné psaní i/í po souhláskách (b, f, m, l, p, s, v, z), " +
                        "které jsou tzv. obojetné, a ke každému ze zmíněných písmen existuje seznam výjimek " +
                        "(tzv. vyjmenovaná slova), u nichž se na místo i/í píše y/ý (např.: východ, pýcha, bylina). " +
                        "Y/ý se píše také po některých přejatých slovech. (Např.: fyziognomie, zygota). (VOJ)";

            } else if (souhlaskyMekke.contains(precedingChar)) {
                return "Patří mezi pravopisně měkké souhlásky (ž, š, č, ř, c, j, ď, ť, ň), " +
                        "které jsou vždy následovány i/í.";
            }

        } else if ((writtenChars.equals("í") && correctChars.equals("ý"))
                || (writtenChars.equals("i") && correctChars.equals("y"))
                && (souhlaskyMekkeObojetne.contains(precedingChar))
                && (lemma.charAt(mistakeCharPosInWord - 2) == precedingChar)
                && (lemma.charAt(mistakeCharPosInWord - 1) == 'y') || (lemma.charAt(mistakeCharPosInWord - 1) == 'ý')
                && ((!tag.startsWith("k2")) || (tag.startsWith("k2") && mistakeCharPosInWord != correctWord.length()))
                && (souhlaskyTvrde.contains(precedingChar))) {
            return "Patří mezi pravopisně tvrdé souhlásky (h ch k r d t n), " +
                    "které jsou v domácích slovech následovány y/ý. (VOJ)";
        }
        return null;
    }

    private String zajmenaMneMeASlovaJeObsahujiciOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        String correctWord = mistake.getCorrectWord();
        Integer mistakeCharPosInWord = mistake.getMistakeCharPosInWord();
        char precedingChar = writtenChars.charAt(mistakeCharPosInWord - 2);
        char nextChar = writtenChars.charAt(mistakeCharPosInWord);

        if ((correctChars.equals("n") && writtenChars.equals(""))
                || (correctChars.equals("") && writtenChars.equals("n"))
                && (precedingChar == 'm') && (nextChar == 'ě')) {
            if (correctWord.length() <= 3) {
                return "Tvar mě se používá ve 2. a 4. pádu (Na mě nezbylo nic). " +
                        "Tvar mně ve 3. a 6. pádu. (Mně se líbí Ivana. -> Komu, čemu?, 3. pád.) " +
                        "Pomůcka: Zaměnit mě/mně za tebe/tobě. Tebe nahrazuje mě (Na tebe nezbylo nic.), " +
                        "tobě nahrazuje mně (Tobě se líbí Ivana.). (VOJ)";

            } else {
                return "Souhláska n po souhlásce m označuje výslovnost [ň + e]; " +
                        "např. měď, měkký. Tam, kde je souhláska [ň] nebo [n] i v jiném tvaru slova " +
                        "nebo v slově příbuzném, píše se mně; např. zapomněl (pomni), rozumně (rozumný).";
            }
        }
        return null;
    }

    //---------------- auxiliary methods -------------------------------------

    private String prefixMatchesSet(String lemmaWithoutAccentsOnIY, Set<String> set) {

        for (String key : set) {
            if (lemmaWithoutAccentsOnIY.startsWith(key)) {
                return key;
            }
        }

        return null;
    }
}
