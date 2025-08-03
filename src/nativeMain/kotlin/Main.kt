import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import raylib.*

const val WIDTH = 800
const val HEIGHT = 600


@OptIn(ExperimentalForeignApi::class)
val TEXT_COLOR: CValue<Color> = cValue {
    r = 255.toUByte()
    g = 0.toUByte()
    b = 0.toUByte()
    a = 255.toUByte()
}

@OptIn(ExperimentalForeignApi::class)
val BG_COLOR: CValue<Color> = cValue {
    r = 255.toUByte()
    g = 255.toUByte()
    b = 255.toUByte()
    a = 255.toUByte()
}


@OptIn(ExperimentalForeignApi::class)
fun main() {
    val songPath = "sounds/song.wav"
    InitAudioDevice()
    val music = LoadMusicStream(songPath)
    InitWindow(WIDTH, HEIGHT, "Raylib with kotlin native")

    var isPlaying = false

    PlayMusicStream(music)
    PauseMusicStream(music)

    while (!WindowShouldClose()) {
        UpdateMusicStream(music)

        if (IsKeyPressed(KEY_SPACE.toInt())) {
            if (isPlaying) {
                PauseMusicStream(music)
                isPlaying = false
            } else {
                ResumeMusicStream(music)
                isPlaying = true
            }
        }

        BeginDrawing()
        ClearBackground(BG_COLOR)
        val message = if (isPlaying) {
            "Press SPACE to pause the music"
        } else {
            "Press SPACE to resume the music"
        }

        drawCenteredText(message)
        EndDrawing()
    }

    UnloadMusicStream(music)
    CloseAudioDevice()
    CloseWindow()
}

@OptIn(ExperimentalForeignApi::class)
fun drawCenteredText(text: String) {
    DrawText(text, WIDTH / 4, HEIGHT / 2, 30, TEXT_COLOR)
}