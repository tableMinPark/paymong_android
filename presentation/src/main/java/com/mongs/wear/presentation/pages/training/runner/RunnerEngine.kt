package com.mongs.wear.presentation.pages.training.runner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.min
import kotlin.math.sqrt

class RunnerEngine(
    private val defaultY: Float,
) {

    companion object {
        private const val TAG = "RunnerEngine"

        private const val GRAVITY = 9.8f
        private const val FRAME = 60L   // 60FPS
        private const val COLLISION_PADDING = 10
        private const val HURDLE_GENERATE_DELAY_MILLIS = 3000

        private const val PLAYER_START_X = 24f
        private const val PLAYER_SPEED = 42f

        private const val HURDLE_START_X = 250f
        private const val HURDLE_SPEED = 3f
    }

    private val _endEvent = MutableSharedFlow<Long>()
    val endEvent = _endEvent.asSharedFlow()

    val playMillis: MutableState<Long> = mutableLongStateOf(0L)
    val isStartGame: MutableState<Boolean> = mutableStateOf(false)
    val score: MutableState<Int> = mutableIntStateOf(0)
    val player: MutableState<Player?> = mutableStateOf(null)
    val hurdleList: MutableList<Hurdle> = mutableListOf()

    /**
     * 시작
     */
    fun start(height: Int, width: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            var generateHurdleCount = 0L
            playMillis.value = 0L
            isStartGame.value = true
            score.value = 0

            // 플레이어 생성
            player.value = Player(
                height = height,
                width = width,
                sy = defaultY,
                sx = PLAYER_START_X,
                ss = PLAYER_SPEED,
                sf = 0.2f,
            )

            while (isStartGame.value) {

                val processStart = LocalDateTime.now()

                // 게임 시간 증가
                playMillis.value += 1000L / FRAME

                // 플레이어 이동
                movePlayer()

                // 장애물 이동
                moveHurdle()

                // 장애물 생성
                if (playMillis.value / HURDLE_GENERATE_DELAY_MILLIS > generateHurdleCount) {
                    // 장애물 5개 당 0.125 씩 스피드 증가
                    val hurdleSpeed = HURDLE_SPEED + (generateHurdleCount / 5 * 0.125f)
                    generateHurdle(hurdleSpeed = hurdleSpeed)
                    generateHurdleCount = playMillis.value / HURDLE_GENERATE_DELAY_MILLIS
                }

                // 좌표 변경 로직 시간 측정
                val processMillis = Duration.between(processStart, LocalDateTime.now()).toMillis()

                // 좌표 변경 로직 시간 제외한 딜레이 진행
                delay(0L.coerceAtLeast(1000L / FRAME - processMillis))
            }

            // 게임 끝 -> 점프 중인 경우 지면에 도착할 때까지 프레임 진행
            player.value?.let {
                while (it.isJump.value) {

                    val processStart = LocalDateTime.now()

                    // 플레이어 이동
                    movePlayer()

                    // 장애물 이동
                    moveHurdle()

                    // 좌표 변경 로직 시간 측정
                    val processMillis = Duration.between(processStart, LocalDateTime.now()).toMillis()

                    // 좌표 변경 로직 시간 제외한 딜레이 진행
                    delay(0L.coerceAtLeast(1000L / FRAME - processMillis))
                }
            }

            _endEvent.emit(System.currentTimeMillis())
        }
    }

    /**
     * 플레이어 이동
     */
    private fun movePlayer() {
        player.value?.move()
    }

    /**
     * 장애물 이동
     */
    private fun moveHurdle() {
        player.value?.let { player ->

            val deleteHurdle = ArrayList<Hurdle>()

            for (hurdle in hurdleList) {
                hurdle.move()

                // 장애물 충돌 확인
                if (isCollision(player = player, hurdle = hurdle)) {
                    isStartGame.value = false
                }
                // 장애물 넘음 확인
                else if (isUnder(player = player, hurdle = hurdle) && !hurdle.isContainedScore) {
                    hurdle.isContainedScore = true
                    score.value++
                }

                // 지나간 장애물 확인 후 적재
                if (hurdle.px.value < -150f) {
                    deleteHurdle.add(hurdle)
                }
            }

            // 지나간 장애물 삭제
            for (hurdle in deleteHurdle) {
                hurdleList.remove(hurdle)
            }
        }
    }

    /**
     * 장애물 생성
     */
    private fun generateHurdle(hurdleSpeed: Float) {
        hurdleList.add(
            Hurdle(
                height = 25,
                width = 35,
                sy = defaultY,
                sx = HURDLE_START_X,
                ss = hurdleSpeed,
            )
        )
    }

    /**
     * 종료
     */
    fun end() {
        isStartGame.value = false
    }

    /**
     * 충돌 여부 확인
     */
    private fun isCollision(player: Player, hurdle: Hurdle) : Boolean {

        val playerMinY = player.py.value - COLLISION_PADDING + player.height
        val playerMaxY = player.py.value + COLLISION_PADDING
        val playerMinX = player.px.value + COLLISION_PADDING
        val playerMaxX = player.px.value - COLLISION_PADDING + player.width
        val hurdleMinY = hurdle.py.value - COLLISION_PADDING + hurdle.height
        val hurdleMaxY = hurdle.py.value + COLLISION_PADDING
        val hurdleMinX = hurdle.px.value + COLLISION_PADDING
        val hurdleMaxX = hurdle.px.value - COLLISION_PADDING + hurdle.width


        val playerRect = listOf(
            Point(playerMinX, playerMinY),
            Point(playerMinX, playerMaxY),
            Point(playerMaxX, playerMaxY),
            Point(playerMaxX, playerMinY),
        )

        val hurdleRect = listOf(
            Point(hurdleMinX, hurdleMinY),
            Point(hurdleMinX, hurdleMaxY),
            Point(hurdleMaxX, hurdleMaxY),
            Point(hurdleMaxX, hurdleMinY),
        )

        val axes = mutableListOf<Point>()

        listOf(playerRect, hurdleRect).forEach { rect ->
            for (i in rect.indices) {
                val p1 = rect[i]
                val p2 = rect[(i + 1) % rect.size]
                val edge = Point(p2.x - p1.x, p2.y - p1.y)
                val normal = Point(-edge.y, edge.x)
                axes.add(normalize(normal))
            }
        }

        for (axis in axes) {
            val (minA, maxA) = projectPolygon(playerRect, axis)
            val (minB, maxB) = projectPolygon(hurdleRect, axis)
            if (!overlap(minA, maxA, minB, maxB)) {
                return false
            }
        }

        return true
    }

    /**
     * 장애물 아래 여부 확인
     */
    private fun isUnder(player: Player, hurdle: Hurdle) : Boolean {

        val playerMinX = player.px.value + COLLISION_PADDING
        val playerMaxX = player.px.value - COLLISION_PADDING + player.width

        val hurdleMinX = hurdle.px.value + COLLISION_PADDING
        val hurdleMaxX = hurdle.px.value - COLLISION_PADDING + hurdle.width

        return (hurdleMinX in playerMinX..playerMaxX) || (hurdleMaxX in playerMinX..playerMaxX)
    }

    class Player(
        private val sy: Float,
        private val sx: Float,
        private val ss: Float,
        private val sf: Float,
        private var jumpSpeed: Float = ss,
        private var jumpFrame: Float = sf,
        val height: Int,
        val width: Int,
        var py: MutableState<Float> = mutableFloatStateOf(sy),
        var px: MutableState<Float> = mutableFloatStateOf(sx),
        var isJump: MutableState<Boolean> = mutableStateOf(false),
    ) {

        /**
         * 점프
         */
        fun jump() {
            if (!isJump.value) {
                this.isJump.value = true
                this.py.value = this.sy
                this.jumpSpeed = this.ss
                this.jumpFrame = this.sf
            }
        }

        /**
         * 이동
         */
        fun move() {
            if (isJump.value) {
                // 스피드 변경
                this.jumpSpeed -= GRAVITY * this.jumpFrame

                // 좌표 변경
                val nextPy = this.py.value - this.jumpSpeed * this.jumpFrame
                this.py.value = min(nextPy, this.sy)

                // 지면 도착 여부 확인
                if (this.py.value == this.sy) {
                    this.isJump.value = false
                    this.jumpSpeed = 0f
                    this.jumpFrame = 0f
                }
            }
        }
    }

    class Hurdle(
        private val sy: Float,
        private val sx: Float,
        private val ss: Float,
        private var speed: Float = ss,
        val height: Int,
        val width: Int,
        var py: MutableState<Float> = mutableFloatStateOf(sy),
        var px: MutableState<Float> = mutableFloatStateOf(sx),
        var isContainedScore: Boolean = false,
    ) {
        fun move() {
            px.value -= this.speed
        }
    }

    data class Point(val x: Float, val y: Float)

    private fun projectPolygon(points: List<Point>, axis: Point): Pair<Float, Float> {
        val projections = points.map { point -> point.x * axis.x + point.y * axis.y }
        return projections.minOrNull()!! to projections.maxOrNull()!!
    }

    private fun overlap(minA: Float, maxA: Float, minB: Float, maxB: Float): Boolean {
        return maxA >= minB && maxB >= minA
    }

    private fun normalize(vector: Point): Point {
        val magnitude = sqrt(vector.x * vector.x + vector.y * vector.y)
        return Point(vector.x / magnitude, vector.y / magnitude)
    }
}