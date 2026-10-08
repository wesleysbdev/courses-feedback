package br.com.fiap.feedback.shared;

import java.security.SecureRandom;
import java.util.UUID;

public final class UuidV7 {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static long lastTimestamp = 0L;
    private static int sequence = 0;

    private UuidV7() { }

    public static synchronized UUID generate() {
        long currentTimestamp = System.currentTimeMillis();

        // Garante monotonicidade caso múltiplos UUIDs sejam gerados no mesmo milissegundo
        if (currentTimestamp <= lastTimestamp) {
            sequence = (sequence + 1) & 0xFFF; // 12 bits de sequência (máx 4095)
            if (sequence == 0) {
                // Se estourar a sequência no mesmo ms, aguarda o próximo
                while (currentTimestamp <= lastTimestamp) {
                    currentTimestamp = System.currentTimeMillis();
                }
            }
        } else {
            sequence = RANDOM.nextInt(4096);
        }
        lastTimestamp = currentTimestamp;

        // 1. Monta os 64 bits mais significativos (MSB)
        // - 48 bits: Timestamp atual (milissegundos)
        // - 4 bits: Versão (0111 = 7)
        // - 12 bits: Sequência / Rand_a
        long msb = (currentTimestamp << 16) | (0x7L << 12) | (sequence & 0xFFF);

        // 2. Monta os 64 bits menos significativos (LSB)
        // - 2 bits: Variante RFC 4122 (10 -> bit mais significativo 63 ligado, 62 desligado)
        // - 62 bits: Bits aleatórios (rand_b)
        long randomBits = RANDOM.nextLong();
        long lsb = 0x8000000000000000L | (randomBits & 0x3FFFFFFFFFFFFFFFL);

        return new UUID(msb, lsb);
    }
}