package main

import (
	"fmt"
	"math"
	"math/rand"
	"os"
	"strconv"
	"strings"

	"github.com/muesli/termenv"
)

type gridItem struct {
	value   int
	flooded bool
}

var grid [][]gridItem
var nFlooded int
var moves int
var p = termenv.ColorProfile()

var colors = []string{
	"#E88388", //red
	"#A8CC8C", //green
	"#DBAB79", //yellow
	"#71BEF2", //blue
	"#D290E4", //magenta
	"#FFFFF",  //gray
}
var bg = termenv.BackgroundColor()

func printGrid() {
	for i := 1; i < len(grid)-1; i++ {
		for j := 1; j < len(grid)-1; j++ {
			elem := grid[i][j]
			elemStr := strconv.Itoa(elem.value)
			out := termenv.String(" " + elemStr + " ")

			if elem.flooded {
				out = out.Foreground(bg).Background(p.Color(colors[elem.value]))
			} else {
				out = out.Foreground(p.Color(colors[elem.value]))
			}
			fmt.Print(out)
		}
		fmt.Println()
	}
	fmt.Println()
}

func initialiseGrid(size int) [][]gridItem {
	tempGrid := make([][]gridItem, size+2)

	for i := 0; i <= size+1; i++ {
		tempGrid[i] = make([]gridItem, size+2)
		for j := 0; j <= size+1; j++ {
			if i == 0 || i == size+1 || j == 0 || j == size+1 {
				tempGrid[i][j] = gridItem{-1, false}
			} else {
				tempGrid[i][j] = gridItem{int(math.Floor(rand.Float64() * 6)), false}
			}
		}
	}
	return tempGrid
}

func flood(x, y, move int) {
	offsets := [][]int{{-1, 0}, {0, -1}, {0, 1}, {1, 0}}
	for _, offset := range offsets {
		i := x + offset[0]
		j := y + offset[1]
		elem := grid[i][j]
		if elem.value == move && !elem.flooded {
			grid[i][j].flooded = true
			nFlooded++
			flood(i, j, move)
		}
	}
}

func playMove(move int) {

	for x, row := range grid {
		for y, elem := range row {
			if elem.flooded {
				// Change the flooded bit
				grid[x][y].value = move
				// And flood it and its neighbours recursively
				flood(x, y, move)
			}
		}
	}
}

func initialise(n int) {
	grid = initialiseGrid(n)
	grid[1][1].flooded = true
	nFlooded = 1
	moves = 0
	flood(1, 1, grid[1][1].value)
}

func parseMoveFromInput(str string) int {
	if strings.TrimSpace(str) == "" {
		return -1
	}

	nextMove, err := strconv.Atoi(str)

	if err != nil {
		return -1
	}
	if nextMove > 6 || grid[1][1].value == nextMove {
		return -1
	}
	return nextMove
}

func parseGridSizeFromArgs(args []string) int {
	if len(args) <= 1 {
		return 5
	}
	parsed, err := strconv.Atoi(args[1])
	if err != nil {
		return 5
	}
	size := int(math.Min(math.Max(5, float64(parsed)), 21))
	return size
}

func main() {
	{
		size := parseGridSizeFromArgs(os.Args)
		initialise(size)

		for {

			termenv.ClearScreen()
			printGrid()
			if nFlooded >= (size * size) {
				break
			}

			for {
				fmt.Println("Moves: " + strconv.Itoa(moves) + " | Flooded: " + strconv.Itoa(nFlooded))
				fmt.Print("Enter move: ")

				str := ""
				fmt.Scanln(&str)
				nextMove := parseMoveFromInput(str)

				if nextMove != -1 {
					moves++
					playMove(nextMove)
					break
				}
				termenv.ClearLines(2)
			}
		}

		fmt.Println("You won in " + strconv.Itoa(moves) + " move(s).")
		fmt.Print("Enter 's' to start again or any other key to quit. ")
		cmd := ""
		fmt.Scanln(&cmd)
		if cmd == "s" {
			main()
		}
	}
}
