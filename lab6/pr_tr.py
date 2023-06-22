import numpy as np

# L1  = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
L1 = [0, 1, 1, 0, 1, 0, 0, 0, 0, 0]
L2 = [1, 0, 0, 1, 0, 0, 0, 0, 0, 0]
# L3  = [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
L3 = [0, 1, 0, 0, 0, 0, 1, 0, 0, 0]
L4 = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
L5 = [0, 0, 0, 0, 0, 1, 1, 0, 0, 0]
L6 = [0, 0, 0, 0, 0, 0, 1, 1, 0, 0]
L7 = [0, 0, 0, 0, 1, 1, 1, 1, 1, 1]
L8 = [0, 0, 0, 0, 0, 0, 1, 0, 1, 0]
L9 = [0, 0, 0, 0, 0, 0, 1, 0, 0, 1]
L10 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

L = np.array([L1, L2, L3, L4, L5, L6, L7, L8, L9, L10])

ITERATIONS = 100


def getM(L):
    M = np.zeros([10, 10], dtype=float)
    # number of outgoing links
    c = np.zeros([10], dtype=int)

    ## DONE =)
    for i in range(10):
        c[i] = sum(L[i])
    print("\n----------------------------------")
    print(c)
    print("----------------------------------")
    for i in range(10):
        for j in range(10):
            if L[j][i] != 0:
                M[i][j] = 1 / c[j]
            else:
                M[i][j] = 0
    return M


print("Matrix L (indices)")
print(L)

M = getM(L)

print("\nMatrix M (stochastic matrix)")
print(M)

### DONE (chyba, bo kilka razy się zakręciłam, zrobiłam fikołka, ale wygląda sensownie) =)
print("\nPAGERANK")

q = 0.15

pr = np.zeros([10], dtype=float)

for i in range(ITERATIONS):
    for j in range(10):
        pr[j] = q + ((1 - q) * sum([pr[k] * M[j][k] for k in range(10)]))
pr = np.sort(pr)[::-1]
for i in range(10):
    print(str(i) + ": " + str(pr[i] / sum(pr)))

### DONE
print("\nTRUSTRANK (DOCUMENTS 1 AND 2 ARE GOOD)")

q = 0.15

d = np.zeros([10], dtype=float)
d[0] = 1
d[1] = 1

tr = [v for v in d]

for i in range(ITERATIONS):
    for j in range(10):
        tr[j] = d[j] * q + ((1 - q) * sum([tr[k] * M[j][k] for k in range(10)]))
tr = np.sort(tr)[::-1]
for i in range(10):
    print(str(i) + ": " + str(tr[i] / sum(tr)))

### CHYBA DONE
print("\nTRUSTRANK (WITHOUT 3->7 AND 1->5)")

L1  = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
# L1 = [0, 1, 1, 0, 1, 0, 0, 0, 0, 0]
L2 = [1, 0, 0, 1, 0, 0, 0, 0, 0, 0]
L3  = [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
# L3 = [0, 1, 0, 0, 0, 0, 1, 0, 0, 0]
L4 = [0, 1, 1, 0, 0, 0, 0, 0, 0, 0]
L5 = [0, 0, 0, 0, 0, 1, 1, 0, 0, 0]
L6 = [0, 0, 0, 0, 0, 0, 1, 1, 0, 0]
L7 = [0, 0, 0, 0, 1, 1, 1, 1, 1, 1]
L8 = [0, 0, 0, 0, 0, 0, 1, 0, 1, 0]
L9 = [0, 0, 0, 0, 0, 0, 1, 0, 0, 1]
L10 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

L = np.array([L1, L2, L3, L4, L5, L6, L7, L8, L9, L10])

M = getM(L)

q = 0.15

d = np.zeros([10], dtype=float)
d[0] = 1
d[1] = 1

tr = [v for v in d]

for i in range(ITERATIONS):
    for j in range(10):
        tr[j] = d[j] * q + ((1 - q) * sum([tr[k] * M[j][k] for k in range(10)]))
tr = np.sort(tr)[::-1]
for i in range(10):
    print(str(i) + ": " + str(tr[i] / sum(tr)))
