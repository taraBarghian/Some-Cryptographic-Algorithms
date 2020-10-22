import queue

plain = [1,0,1,1,1,1,1,0,1,1,1,0,1,0]

a = queue.Queue(maxsize=93)
al = [1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
b = queue.Queue(maxsize=84)
bl = [1, 0, 0, 1, 0, 0, 0]
c = queue.Queue(maxsize=111)
cl = [1, 1]

# initialize given registers
for i in range(93):
    if i < len(al):
        a.put(al[-i - 1])
    else:
        a.put(0)

for i in range(84):
    if i < len(bl):
        b.put(bl[-i - 1])
    else:
        b.put(0)

for i in range(111):
    if i < len(cl):
        c.put(cl[-i - 1])
    else:
        c.put(0)

a_i, b_i, c_i = 0, 0, 0
a_o, b_o, c_o = 0, 0, 0
key = []

# Trivium algorithm
for i in range(14):
    al, bl, cl = list(a.queue), list(b.queue), list(c.queue)
    a_o = a.get() ^ (al[-1] & al[-2]) ^ al[66]
    a_i = a_o ^ al[69]
    b_o = b.get() ^ (bl[-1] & bl[-2]) ^ bl[69]
    b_i = b_o ^ bl[78]
    c_o = c.get() ^ (cl[-1] & cl[-2]) ^ cl[66]
    c_i = c_o ^ al[87]
    a.put(c_i ^ al[69])
    b.put(a_i ^ al[78])
    c.put(b_i ^ al[87])
    key.append(a_o ^ b_o ^ c_o)


print(key)
print(list(plain))
print([i^j for i,j in zip(plain,key)])

