package com.laofan.strangetask.task.keywordFrequncy.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class GoogleSpeechService {
        /**
         * Demonstrates using the Speech API to transcribe an audio file.
         */
        public static void main(String... args) throws Exception {
            // Instantiates a client
            try (SpeechClient speechClient = SpeechClient.create()) {

                // The path to the audio file to transcribe
                String fileName = "./resources/audio/lfasr.wav";

                // Reads the audio file into memory
                Path path = Paths.get(fileName);
                byte[] data = Files.readAllBytes(path);
                ByteString audioBytes = ByteString.copyFrom(data);

                // Builds the sync recognize request
                RecognitionConfig config = RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setSampleRateHertz(16000)
                        .setLanguageCode("en-US")
                        .build();
                RecognitionAudio audio = RecognitionAudio.newBuilder()
                        .setContent(audioBytes)
                        .build();

                // Performs speech recognition on the audio file
                RecognizeResponse response = speechClient.recognize(config, audio);
                List<SpeechRecognitionResult> results = response.getResultsList();

                for (SpeechRecognitionResult result : results) {
                    // There can be several alternative transcripts for a given chunk of speech. Just use the
                    // first (most likely) one here.
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    System.out.printf("Transcription: %s%n", alternative.getTranscript());
                }
            }
        }
}
