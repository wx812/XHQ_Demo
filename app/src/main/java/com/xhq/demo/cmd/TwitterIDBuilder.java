package com.xhq.demo.cmd;

/**
 * @author : AKzz
 * @version : 1.10
 * PK生成器
 */
@SuppressWarnings("UnusedDeclaration")
class TwitterIDBuilder {
    private int datacenter_id = 0;
    private int worker_id = 0;
    // ======================================================================================
    // id:[ timestamp  datacenter_id   worker_id   sequence]
    //    [    42     |     5        |    5      |     12  ]
    // ======================================================================================
    private static final long twepoch = 1288834974657L;  // (Thu, 04 Nov 2010 01:42:54 GMT)
    private static final int worker_id_bits = 5;
    private static final int datacenter_id_bits = 5;
    private static final int sequence_bits = 12;

    private static final int worker_id_shift = sequence_bits;
    private static final int datacenter_id_shift = worker_id_bits + sequence_bits;
    private static final int timestamp_left_shift = datacenter_id_bits + worker_id_bits + sequence_bits;
    private static final int sequence_mask = (~(-1 << 12)); // ((1 << 12) - 1)

    private static class TwittterIdWorker {
        long lastTimestamp;
        int dataCenterId;
        int wokerId;
        int sequence;
        private TwittterIdWorker(int datacenter_id, int worker_id) {
            this.lastTimestamp = 0L;
            this.dataCenterId = datacenter_id;
            this.wokerId = worker_id;
            this.sequence = 0;
            this.lastTimestamp = -1L;
        }

        public long nextId() {
            long timestamp = this.timeGen();
            if (timestamp == this.lastTimestamp) {
                this.sequence = (this.sequence + 1) & sequence_mask;
                if (this.sequence == 0) {
                    timestamp = tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0;
            }

            this.lastTimestamp = timestamp;
            //noinspection UnnecessaryLocalVariable
            long nextId = ((timestamp - twepoch) << timestamp_left_shift) | (long) (this.dataCenterId << datacenter_id_shift) | (long) (this.wokerId << worker_id_shift) | (long) this.sequence;
            return nextId;
        }

        private long tilNextMillis(final long lastTimestamp) {
            long timestamp = this.timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = this.timeGen();
            }
            return timestamp;
        }

        private long timeGen() {
            return System.currentTimeMillis();
        }
    }

    private TwittterIdWorker ukey = null;

    public TwitterIDBuilder(int datacenter_id, int worker_id) {
        if (datacenter_id > 0x1f) throw new IllegalArgumentException("datacenter_id 超过了最大值 " + 0x1f + "!");
        if (worker_id > 0x1f) throw new IllegalArgumentException("worker_id 超过了最大值 " + 0x1f + "!");
        this.ukey = new TwittterIdWorker(datacenter_id, worker_id);
    }

    public long newNextId2() {
        long v = 0L;
        synchronized (this) {
            v = this.ukey.nextId();
        }
        return v;
    }

    public String newNextId() {
        return String.valueOf(newNextId2());
    }
}
