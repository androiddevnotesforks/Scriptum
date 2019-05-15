package sgtmelon.scriptum.test.main

import androidx.test.espresso.Espresso.closeSoftKeyboard
import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест работы [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }


    @Test fun contentEmpty() {
        beforeLaunch { testData.clear() }

        MainScreen { openRankPage { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.clear().fillRank() }

        MainScreen { openRankPage { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.clear().fillRank(times = 20) }

        MainScreen {
            openRankPage {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }


    @Test fun toolbarEnterAddEmpty() {
        beforeLaunch { testData.clear() }

        MainScreen {
            openRankPage {
                toolbar {
                    assert { isAddEnabled(enabled = false) }
                    onEnterName(name = " ")
                    assert { isAddEnabled(enabled = false) }
                }
            }
        }
    }

    @Test fun toolbarEnterAddFromList() {
        val rankItem = testData.clear().insertRank()

        afterLaunch {
            MainScreen {
                openRankPage {
                    toolbar {
                        assert { isAddEnabled(enabled = false) }
                        onEnterName(rankItem.name)
                        assert { isAddEnabled(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnabled() {
        beforeLaunch { testData.clear() }

        MainScreen {
            openRankPage {
                toolbar {
                    assert { isAddEnabled(enabled = false) }
                    onEnterName(testData.rankItem.name)
                    assert { isAddEnabled(enabled = true) }
                }
            }
        }
    }

    @Test fun toolbarEnterClear() {
        beforeLaunch { testData.clear() }

        MainScreen {
            openRankPage {
                toolbar {
                    assert { isClearEnabled(enabled = false) }
                    onEnterName(testData.rankItem.name)
                    assert { isClearEnabled(enabled = true) }
                    onClickClear()
                    assert { isClearEnabled(enabled = false) }
                }
            }
        }
    }

    @Test fun toolbarEnterAddStart() {
        val name = testData.apply {
            clear()
            insertRank()
        }.uniqueString

        afterLaunch {
            MainScreen {
                openRankPage {
                    toolbar {
                        onEnterName(name)
                        onLongClickAdd()
                    }

                    assert { onDisplayContent(empty = false) }
                    openRenameDialog(name)
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnd() {
        val name = testData.apply {
            clear()
            insertRank()
        }.uniqueString

        afterLaunch {
            MainScreen {
                openRankPage {
                    toolbar {
                        onEnterName(name)
                        onClickAdd()
                    }

                    assert { onDisplayContent(empty = false) }
                    openRenameDialog(name, p = count - 1)
                }
            }
        }
    }


    @Test fun rankVisibleAnimationClick() {
        val rankItem = testData.clear().insertRank()

        afterLaunch {
            MainScreen {
                openRankPage {
                    onClickVisible(rankItem.name)
                    waitBefore(time = 500) { onClickVisible(rankItem.name) }
                }
            }
        }
    }

    @Test fun rankVisibleAnimationLongClick() {
        val rankList = testData.clear().fillRank(times = 5)

        afterLaunch {
            MainScreen {
                openRankPage {
                    onLongClickVisible(rankList[0].name)
                    waitBefore(time = 500) { onLongClickVisible(rankList[count - 1].name) }
                }
            }
        }
    }

    @Test fun rankVisibleForNotes() {
        val rankList = testData.clear().insertRankToBin()

        afterLaunch {
            MainScreen {
                openRankPage { onClickVisible(rankList[1].name) }
                openBinPage { assert { onDisplayContent(empty = false) } }
                openRankPage { onClickVisible(rankList[0].name) }
                openBinPage { assert { onDisplayContent(empty = true) } }
            }
        }
    }

    @Test fun rankVisibleForBin() {

    }

    @Test fun rankClearFromList() {
        val rankItem = testData.clear().insertRank()

        afterLaunch {
            MainScreen {
                openRankPage {
                    onClickCancel(rankItem.name)
                    assert { onDisplayContent(empty = true) }
                }
            }
        }
    }

    @Test fun rankClearForNote() {
        val rankList = testData.clear().insertRankToNotes()

        afterLaunch {
            MainScreen {
                openRankPage { onClickVisible(rankList[0].name) }
                openNotesPage { assert { onDisplayContent(empty = true) } }
                openRankPage { onClickCancel(rankList[0].name) }
                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }


    @Test fun renameDialogOpen() {
        val rankItem = testData.clear().insertRank()

        afterLaunch { MainScreen { openRankPage { openRenameDialog(rankItem.name) } } }
    }

    @Test fun renameDialogCloseSoft() {
        val rankItem = testData.clear().insertRank()

        afterLaunch {
            MainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) {
                        closeSoftKeyboard()
                        onCloseSoft()
                    }

                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun renameDialogCloseCancel() {
        val rankItem = testData.clear().insertRank()

        afterLaunch {
            MainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) { onClickCancel() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun renameDialogBlockApplySameName() {
        val rankItem = testData.clear().insertRank()

        afterLaunch {
            MainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) {
                        onEnterName(rankItem.name)
                        assert { isAcceptEnable(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun renameDialogBlockApplyFromList() {
        val listRank = testData.clear().fillRank(times = 2)

        afterLaunch {
            MainScreen {
                openRankPage {
                    openRenameDialog(listRank[0].name) {
                        onEnterName(listRank[1].name)
                        assert { isAcceptEnable(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun renameDialogResult() {
        val rankItem = testData.clear().insertRank()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) {
                        onEnterName(newName)
                        assert { isAcceptEnable(enabled = true) }
                        onClickAccept()
                    }

                    assert { onDisplayContent(empty = false) }

                    openRenameDialog(newName)
                }
            }
        }
    }

}