import sys
import argparse
import random

parser = argparse.ArgumentParser(description='Generate humidity sensor data.')
parser.add_argument('num_rows', metavar='n', type=int, help='Number of rows to generate')
parser.add_argument('outfile', nargs='?', type=argparse.FileType('w'), default=sys.stdout)

args = parser.parse_args()
args.outfile.write('sensor-id,humidity\n')
for i in range(args.num_rows):
    sid = random.randint(0, 10)
    val = random.randint(-50, 100)
    line = 's{},{}\n'.format(sid, 'NaN' if val < 0 else val)
    args.outfile.write(line)
