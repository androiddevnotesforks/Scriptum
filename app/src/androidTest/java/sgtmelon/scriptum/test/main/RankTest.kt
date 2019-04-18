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
        beforeLaunch { testData.clearAllData() }

        MainScreen { rankScreen { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank() }

        MainScreen { rankScreen { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank(times = 20) }

        MainScreen {
            rankScreen {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }


    @Test fun toolbarEnterAddEmpty() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            rankScreen {
                toolbar {
                    assert { isAddEnabled(enabled = false) }
                    onEnterName(name = " ")
                    assert { isAddEnabled(enabled = false) }
                }
            }
        }
    }

    @Test fun toolbarEnterAddFromList() {
        val rankItem = testData.apply { clearAllData() }.insertRank()

        afterLaunch {
            MainScreen {
                rankScreen {
                    toolbar {
                        assert { isAddEnabled(enabled = false) }
                        onEnterName(rankItem.name)
                        assert { isAddEnabled(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnable() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            rankScreen {
                toolbar {
                    assert { isAddEnabled(enabled = false) }
                    onEnterName(testData.rankItem.name)
                    assert { isAddEnabled(enabled = true) }
                }
            }
        }
    }

    @Test fun toolbarEnterClear() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            rankScreen {
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
            clearAllData()
            insertRank()
        }.uniqueString

        afterLaunch {
            MainScreen {
                rankScreen {
                    toolbar {
                        onEnterName(name)
                        onLongClickAdd()
                    }

                    assert { onDisplayContent(empty = false) }
                    renameDialogUi(name)
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnd() {
        val name = testData.apply {
            clearAllData()
            insertRank()
        }.uniqueString

        afterLaunch {
            MainScreen {
                rankScreen {
                    toolbar {
                        onEnterName(name)
                        onClickAdd()
                    }

                    assert { onDisplayContent(empty = false) }
                    renameDialogUi(name, p = count - 1)
                }
            }
        }
    }


    @Test fun rankVisibleAnimationClick() {
        val rankItem = testData.apply { clearAllData() }.insertRank()

        afterLaunch {
            MainScreen {
                rankScreen {
                    onClickVisible(rankItem.name)
                    wait(time = 500) { onClickVisible(rankItem.name) }
                }
            }
        }
    }

    @Test fun rankVisibleAnimationLongClick() {
        val rankList = testData.apply { clearAllData() }.fillRank(times = 5)

        afterLaunch {
            MainScreen {
                rankScreen {
                    onLongClickVisible(rankList[0].name)
                    wait(time = 500) { onLongClickVisible(rankList[count - 1].name) }
                }
            }
        }
    }

    @Test fun rankVisibleForNotes() {
        val rankList = testData.apply { clearAllData() }.insertRankToBin()

        afterLaunch {
            MainScreen {
                rankScreen { onClickVisible(rankList[1].name) }
                binScreen { assert { onDisplayContent(empty = false) } }
                rankScreen { onClickVisible(rankList[0].name) }
                binScreen { assert { onDisplayContent(empty = true) } }
            }
        }
    }

    @Test fun rankVisibleForBin() {

    }

    @Test fun rankClearFromList() {
        val rankItem = testData.apply { clearAllData() }.insertRank()

        afterLaunch {
            MainScreen {
                rankScreen {
                    onClickCancel(rankItem.name)
                    assert { onDisplayContent(empty = true) }
                }
            }
        }
    }

    @Test fun rankClearForNote() {
        val rankList = testData.apply { clearAllData() }.insertRankToNotes()

        afterLaunch {
            MainScreen {
                rankScreen { onClickVisible(rankList[0].name) }
                notesScreen { assert { onDisplayContent(empty = true) } }
                rankScreen { onClickCancel(rankList[0].name) }
                notesScreen { assert { onDisplayContent(empty = false) } }
            }
        }
    }


    @Test fun renameDialogOpen() {
        val rankItem = testData.apply { clearAllData() }.insertRank()

        afterLaunch { MainScreen { rankScreen { renameDialogUi(rankItem.name) } } }
    }

    @Test fun renameDialogCloseSoft() {
        val rankItem = testData.apply { clearAllData() }.insertRank()

        afterLaunch {
            MainScreen {
                rankScreen {
                    renameDialogUi(rankItem.name) {
                        closeSoftKeyboard()
                        onCloseSoft()
                    }

                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun renameDialogCloseCancel() {
        val rankItem = testData.apply { clearAllData() }.insertRank()

        afterLaunch {
            MainScreen {
                rankScreen {
                    renameDialogUi(rankItem.name) { onClickCancel() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun renameDialogBlockApplySameName() {
        val rankItem = testData.apply { clearAllData() }.insertRank()

        afterLaunch {
            MainScreen {
                rankScreen {
                    renameDialogUi(rankItem.name) {
                        onEnterName(rankItem.name)
                        assert { isAcceptEnable(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun renameDialogBlockApplyFromList() {
        val listRank = testData.apply { clearAllData() }.fillRank(times = 2)

        afterLaunch {
            MainScreen {
                rankScreen {
                    renameDialogUi(listRank[0].name) {
                        onEnterName(listRank[1].name)
                        assert { isAcceptEnable(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun renameDialogResult() {
        val rankItem = testData.apply { clearAllData() }.insertRank()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                rankScreen {
                    renameDialogUi(rankItem.name) {
                        onEnterName(newName)
                        assert { isAcceptEnable(enabled = true) }
                        onClickAccept()
                    }

                    assert { onDisplayContent(empty = false) }

                    renameDialogUi(newName)
                }
            }
        }
    }

}