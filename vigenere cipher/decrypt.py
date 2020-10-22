from collections import Counter

m = 5
cipher = "pbbsadieofxefsetexpepipwarmzgvdxqrofmxrvykfcvygabgtrgoydspwfevgggdtdcazyzqrmcgbjtpxwarpuggprubtelubtosfofeiabjpqmbbcluaulhichyhggrqsgbqssbsqzjpwfevgggdmyarommhrprvclxizhpfvucftxkrbxedwnyrqbhxidchdwmkgssguuewfvrsyychcipgrprkchpcqgfzrevbhjmfghsgbnqjqqgphabrlpfselxucaltmfgxizhfnixsocefsqmyfavohxsgzrewaeidsfeiprrdgdwophpsstgusaeebdylypsqnszgvdxqrzjqqrbaeegroipknchfkbeexsaeirtrnxesrxipsaressftbablpkdsnehaqutppgbcvkzvgipdezgqsqnsfhnriroewifhrcwmgulqqrtpxozbelqgqlcehntvefrrvqhnemranexqfgzszofyiqrroexabdxmholwwsgciyovyfkwzavajrowqbftfxsfpvhoaewovvwhdsadxdwxtrswadyddetwqabyxtgbycqogmcqggpiyrrdmdsjlvyhuqsdarcwgfrelmhgsefknjkmjrlrktbyhzcjsmepbjqurqwifcadmdbbcizuezwesqljrspemabrigqzyprfrvdwuavwedqbxtxwzprfqhwxujneipdepjqfrygqsnewgtstgusaeqmmjppxbrixpcbcwacahiyfupjaielwewfeezqrtqbfrdwucadifwadmbwqtxkbbhgabapgfwbysrtlzyecytguhhoigbqpvmgfpiygjpqqggfjrhuzwqggjpqogwmehrymzufseysypwepllfuzvemqgccszchygqcudyedrnxqrvdertrnxucayijhvehdojtrpfnhqgqumvqrfemxzpzydhazwyoywxtwavhqogswaoahvahrtroczxspsapgqgflvkbbtxnsulzuchcgabitrosqomehefwfgnyyztrppubtsiochwhpsnelewanipcjpladromewapbcivdmfsazqkogeizhvzrqlgprewipxtsqpxqfztrqqbyzqmvykycbypuuueessngsurszvesrxedfldsdflnluzqdmfhvykecgzxmzyjjafolhqvhyhdsqesyolxyewplpmfetzmzoppajropgqxtpkoqltfsqsmygujrqgfxizhvzryoecmqrfzrevrsmegglvfsqysifbdiutndtmggyimfjpvqhbrvmqrqyxvrppqunygqcuxspselxqogeizrroizhelroscwimghcihiyredgnhjmhffhpsaphioeoammcweksqpmfvrcxtchrlfgfxexzrdxmhbctqqhwmmfeppmhvzrnfrphubtavarhnipoalxpsclvfgctvuhbywfovcwevrelqsvelqfnciiwfosydelmesgsmzufdlqprqsdsopqahupvuhfpprjnymfmslzaieosysbqfquvywqljlwbcjpvvclljfsesepknwpeavwie"

def clearing(string):
    string = string.replace(".", "")
    string = string.replace(" ", "")
    return string.lower()


def decrypt(cipher):
    l = len(cipher) // m + 1
    list = [cipher[i * m:i * m + m] for i in range(l)] #Separate cipher text

    if (len(list[-1]) != m):
        list[-1] += (m - len(list[-1])) * " "

    all_freq = [""] * m
    for i in list:
        for j in range(m):
            all_freq[j] += i[j]

    freqs = [(Counter(item)) for item in all_freq] #Find frequencies of cipher text

    key = ""
    for i in range(m):
        most = (freqs[i].most_common(1))
        key+=chr(ord(most[0][0])-ord('e')+ord('a')) #find key using freq table -> e ~ 12%    t ~ 9% ...

    plain_text = ""
    for item in list: #decrypt
        x = [chr((ord(item[i])-ord(key[i]))%26 + ord('a')) for i in range(m)]
        plain_text+="".join(x)
    print("key = " + key)
    return plain_text


plain = decrypt(clearing(cipher))
print(plain)
